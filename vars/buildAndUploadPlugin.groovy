import groovy.json.JsonSlurper

def call() {
    try {
        git(
                url: 'git@github.com:lsfusion/plugin-idea.git',
                branch: 'master',
        )

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
