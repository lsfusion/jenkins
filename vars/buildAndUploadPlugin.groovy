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

            try {
                withCredentials([string(credentialsId: 'jetbrains.plugins.token', variable: 'token')]) {
                    sh "./gradlew publishPlugin -PintellijPublishToken=${token}"
                }
            } catch (e) {
                echo "Failed to publish plugin: ${e.message}"
                return
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
        slack.error "Warning! <$env.BUILD_URL|$currentBuild.fullDisplayName> failed."
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