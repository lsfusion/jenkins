def call() {
    try {
        git(
                url: 'git@github.com:lsfusion/plugin-idea.git',
                branch: 'master',
        )

        def versionFile = "${Paths.jenkinsHome}/currentPluginVersion"
        def oldVer = readFile versionFile
        def pluginXml = readFile "META-INF/plugin.xml"
        def newVer = getPluginVersion(pluginXml)
        if (newVer != oldVer) {
            sh 'chmod +x gradlew'
            sh './gradlew buildPlugin'

            sh "cp -f build/distributions/lsfusion-idea-plugin-${newVer}.zip lsfusion-idea-plugin.zip"

            withCredentials([string(credentialsId: 'jetbrains.plugins.token', variable: 'token')]) {
                sh "./gradlew publishPlugin -PintellijPublishToken=${token}"
            }

            ftpPublisher failOnError: true, publishers: [
                    [configName: 'Download FTP server',
                     transfers : [
                             [sourceFiles: "lsfusion-idea-plugin.zip", remoteDirectory: "exe/ext"]
                     ],
                     verbose   : true]
            ]

            slack.message "Plugin v.${newVer} was built successfully.\n```${getReleaseNotes(pluginXml)}```"

            writeFile file: versionFile, text: newVer
        } else {
            echo "version's the same"
        }
    } catch (e) {
        slack.error "Warning! <$env.BUILD_URL|$currentBuild.fullDisplayName> failed."
        throw e
    }
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