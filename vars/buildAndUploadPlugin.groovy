import groovy.json.JsonSlurper

def call() {
    try {
        git(
                url: 'git@github.com:lsfusion/plugin-idea.git',
                branch: 'master',
        )
        def builtCommit = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()

        def response = sh(
                script: "curl -s https://plugins.jetbrains.com/api/plugins/7601/updates",
                returnStdout: true
        ).trim()
        def oldVer = parseLatestVersion(response)
        def pluginXml = readFile "META-INF/plugin.xml"
        def newVer = getPluginVersion(pluginXml)

        if (newVer != oldVer) {
            sh 'chmod +x gradlew'
            sh './gradlew buildPlugin'

            sh "cp -f build/distributions/lsfusion-idea-plugin-${newVer}.zip lsfusion-idea-plugin.zip"

            def publishStatus
            withCredentials([string(credentialsId: 'jetbrains.plugins.token', variable: 'token')]) {
                // Single-quoted: the shell expands $token, so the secret is not interpolated by Groovy.
                publishStatus = sh(
                        script: '#!/bin/bash\nset -o pipefail\n./gradlew publishPlugin -PintellijPublishToken="$token" 2>&1 | tee build/publishPlugin.log',
                        returnStatus: true
                )
            }
            if (publishStatus != 0) {
                def publishLog = readFile 'build/publishPlugin.log'
                // oldVer is the latest approved update, so every push rebuilds a version that is still waiting for review
                // and Marketplace refuses the second upload. That is expected; any other failure is not.
                if (publishLog.contains('already contains version')) {
                    echo "Version ${newVer} is already uploaded to JetBrains Marketplace and not approved yet, nothing is published."
                    currentBuild.description = "${newVer}: already uploaded, not approved yet"
                    return
                }
                error "Publishing ${newVer} to JetBrains Marketplace failed: ${gradleFailureReason(publishLog)}"
            }

            // Marketplace has the version now, whatever happens to the FTP upload, so it is tagged right away.
            tagRelease(newVer, builtCommit)

            ftpPublisher failOnError: true, publishers: [
                    [configName: 'Download FTP server',
                     transfers : [
                             [sourceFiles: "lsfusion-idea-plugin.zip", remoteDirectory: "exe/ext"]
                     ],
                     verbose   : true]
            ]

            slack.message "Plugin v${newVer} was built successfully.\n```${getReleaseNotes(pluginXml)}```"
        } else {
            echo "Version ${newVer} matches the latest version in Marketplace. Skipping build."
        }
    } catch (e) {
        slack.error "Warning! <$env.BUILD_URL|$currentBuild.fullDisplayName> failed${e.message ? ': ' + e.message : '.'}"
        throw e
    }
}

// Tags the version this build uploaded as v<version> on commit, the one it was built from, in lsfusion/plugin-idea, so
// that the commit of what the Marketplace has can be found from the version. A tag on another commit is moved: it is of
// an upload the Marketplace no longer has, as when an update it rejected is deleted there and the fixed plugin is uploaded
// with the same version.
// A failure leaves the release as it is: the build turns unstable and Slack gets a warning. No later build retries it, they
// end on "already contains version" until the review is over, so the tag is then put by hand.
def tagRelease(String version, String commit) {
    def tag = "v${version}"
    try {
        def tagged = remoteTagCommit(tag)
        if (tagged == commit) {
            echo "${tag} is already on ${tagged}."
            return
        }
        // -f: a build that failed to push the tag has left it in this checkout
        sh "git tag -f -a ${tag} -m 'Release ${version}' ${commit}"
        sh "git push --force origin refs/tags/${tag}"
        echo(tagged == null ? "Tagged ${commit} as ${tag}." : "Moved ${tag} from ${tagged} to ${commit}.")
    } catch (InterruptedException e) {
        throw e // an aborted build stops here, it does not go on to the FTP upload
    } catch (e) {
        def reason = e.message ?: e.toString()
        echo "Tagging ${commit} as ${tag} failed: ${reason}"
        currentBuild.result = 'UNSTABLE'
        currentBuild.description = "${currentBuild.description ?: version}, not tagged"
        slack.warning "Plugin v${version} is uploaded, but <$env.BUILD_URL|$currentBuild.fullDisplayName> did not create the tag ${tag}: ${reason}."
    }
}

// The commit that tag is on in lsfusion/plugin-idea, null if there is no such tag.
def remoteTagCommit(String tag) {
    def ref = "refs/tags/${tag}"
    // An annotated tag is listed as the tag object, and as the commit only when ref^{} is asked for.
    def lsRemote = sh(script: "git ls-remote origin '${ref}' '${ref}^{}'", returnStdout: true)
    return lsRemoteCommit(lsRemote, ref)
}

@NonCPS
def lsRemoteCommit(String lsRemote, String ref) {
    def shas = [:]
    lsRemote.eachLine { line ->
        def parts = line.trim().split(/\s+/)
        if (parts.length == 2) {
            shas[parts[1]] = parts[0]
        }
    }
    return shas[ref + '^{}'] ?: shas[ref]
}

@NonCPS
def parseLatestVersion(String jsonText) {
    def json = new JsonSlurper().parseText(jsonText)
    if (json instanceof List && !json.isEmpty()) {
        return json[0].version.toString()
    }
    return ""
}

@NonCPS
def getPluginVersion(String xml) {
    return new XmlSlurper().parseText(xml).version.text()
}

@NonCPS
def getReleaseNotes(String text) {
    def startIndex = text.indexOf("<ul>")
    def endIndex = text.indexOf("</ul>")
    if (startIndex == -1 || endIndex == -1) return ""
    def lis = new XmlSlurper().parseText(text.substring(startIndex, endIndex + 5))
    def res = ""
    for (li in lis.children()) {
        res = "${res}• ${li.text()}\n"
    }
    return res
}

// Gradle's "* What went wrong:" section as one line, e.g.
// "Execution failed for task ':verifyPlugin'. Verification failed with [INTERNAL_API_USAGES] problems. ..."
@NonCPS
def gradleFailureReason(String log) {
    def lines = log.readLines()
    def start = lines.findIndexOf { it.startsWith('* What went wrong:') }
    if (start == -1) {
        return 'no Gradle failure message, see the build log'
    }
    return lines.drop(start + 1)
            .takeWhile { !it.trim().isEmpty() }
            .collect { it.replaceFirst(/^>\s*/, '').trim() }
            .join(' ')
            .take(300)
}
