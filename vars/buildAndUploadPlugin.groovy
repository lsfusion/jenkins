import groovy.json.JsonOutput
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
        def pluginXml = readFile file: "META-INF/plugin.xml", encoding: 'UTF-8'
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

            // Marketplace has the version now, whatever happens to the FTP upload, so it is tagged and released right away.
            tagRelease(newVer, builtCommit)
            def releaseUrl = publishGithubRelease(newVer, builtCommit, getChangeNotes(pluginXml), "build/distributions/lsfusion-idea-plugin-${newVer}.zip")

            ftpPublisher failOnError: true, publishers: [
                    [configName: 'Download FTP server',
                     transfers : [
                             [sourceFiles: "lsfusion-idea-plugin.zip", remoteDirectory: "exe/ext"]
                     ],
                     verbose   : true]
            ]

            // a link rather than the change notes themselves, which are too long for a Slack message; without the release,
            // to the change notes in plugin.xml
            def notesLink = releaseUrl ? "<${releaseUrl}|release notes>" :
                    "<https://github.com/lsfusion/plugin-idea/blob/${builtCommit}/META-INF/plugin.xml|change notes in plugin.xml>"
            slack.message "Plugin v${newVer} was built successfully: ${notesLink}."
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

// Publishes the version this build uploaded as the GitHub release v<version> of lsfusion/plugin-idea, with the change notes
// and the zip, so that any version can be downloaded without the Marketplace and installed with Install Plugin from Disk:
// lsfusion.org has the latest one only. A release of that version is updated and its zip replaced: like the tag (see
// tagRelease), it is of an upload the Marketplace no longer has.
// It is a full release from the start, not a prerelease until the review is over, as lsfusion.org serves the version
// from the start too.
// A failure leaves the uploads as they are: the build turns unstable, archives the notes and the zip, and Slack gets a
// warning. No later build retries it, as with the tag, so the release is then published by hand from those files.
// Returns the URL of the release, null if it failed.
def publishGithubRelease(String version, String commit, String notes, String zip) {
    String tag = "v${version}"
    String asset = "lsfusion-idea-plugin-${version}.zip"
    String notesFile = 'build/release-notes.md'
    String repo = 'repos/lsfusion/plugin-idea'
    String url = null
    try {
        writeFile file: notesFile, text: notes, encoding: 'UTF-8'
        withCredentials([string(credentialsId: 'api.github.com.token', variable: 'TOKEN')]) {
            def release = githubApi('GET', "https://api.github.com/${repo}/releases/tags/${tag}", [200, 404])
            if (release == null) {
                // target_commitish is for a tag that was not pushed: GitHub then puts it on that commit, not on master
                release = githubApi('POST', "https://api.github.com/${repo}/releases", [201],
                        [tag_name: tag, target_commitish: commit, name: version, body: notes])
            } else {
                githubApi('PATCH', "https://api.github.com/${repo}/releases/${release.id}", [200], [name: version, body: notes])
                for (int i = 0; i < release.assets.size(); i++) {
                    if (release.assets[i].name == asset) {
                        githubApi('DELETE', "https://api.github.com/${repo}/releases/assets/${release.assets[i].id}", [204])
                    }
                }
            }
            githubApi('POST', "https://uploads.github.com/${repo}/releases/${release.id}/assets?name=${asset}", [201], null, zip)
            url = release.html_url
            echo "Published ${zip} in ${url}."
        }
    } catch (InterruptedException e) {
        throw e // an aborted build stops here, it does not go on to the FTP upload
    } catch (e) {
        def reason = e.message ?: e.toString()
        echo "Publishing the GitHub release ${tag} failed: ${reason}"
        currentBuild.result = 'UNSTABLE'
        currentBuild.description = "${currentBuild.description ?: version}, no GitHub release"
        slack.warning "Plugin v${version} is uploaded, but <$env.BUILD_URL|$currentBuild.fullDisplayName> did not publish the GitHub release ${tag}: ${reason}."
        try {
            // they stay in this checkout only until the next build
            archiveArtifacts artifacts: "${zip},${notesFile}", allowEmptyArchive: true
        } catch (InterruptedException ie) {
            throw ie
        } catch (ae) {
            echo "Archiving the notes and the zip failed: ${ae.message ?: ae.toString()}"
        }
    }
    return url
}

// Sends a request to the GitHub API with the token in TOKEN and returns the response, data as JSON or else file as a zip.
// Fails on a status other than expected, returns null on an expected 404.
def githubApi(String method, String url, List expected, Map data = null, String file = null) {
    String request = 'build/github-request.json'
    String response = 'build/github-response.json'
    String body = ''
    if (data != null) {
        writeFile file: request, text: JsonOutput.toJson(data), encoding: 'UTF-8'
        body = "-H 'Content-Type: application/json' --data-binary '@${request}'"
    } else if (file != null) {
        body = "-H 'Content-Type: application/zip' --data-binary '@${file}'"
    }
    // \$TOKEN: the shell expands it, so the secret is not interpolated by Groovy
    int status = sh(returnStdout: true, script: "rm -f ${response}; curl -sS -o ${response} -w '%{http_code}' -X ${method} " +
            "-H \"Authorization: Bearer \$TOKEN\" -H 'Accept: application/vnd.github+json' -H 'X-GitHub-Api-Version: 2022-11-28' " +
            "${body} '${url}'").trim().toInteger()
    String text = fileExists(response) ? readFile(response).trim() : ''
    // an error page of a proxy in front of GitHub is not JSON
    def json = text.startsWith('{') || text.startsWith('[') ? readJSON(text: text) : null
    if (!expected.contains(status)) {
        String message = json instanceof Map && json.message ? ": ${json.message}${json.errors ? ' ' + json.errors : ''}" : ''
        error "GitHub answered ${method} ${url} with ${status}${message}"
    }
    return status == 404 ? null : json
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

// The change notes of plugin.xml for a GitHub release. GitHub renders their HTML (<b>, <ul>, <li>, <code>, <br>) as it is,
// once no line is indented: Markdown shows an indented line as code.
@NonCPS
def getChangeNotes(String xml) {
    return new XmlSlurper().parseText(xml).'change-notes'.text().readLines()*.trim().findAll().join('\n')
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
