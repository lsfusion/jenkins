def call() {
    try {
        git(
                url: 'https://github.com/lsfusion/plugin-idea',
                branch: 'master',
        )

        def versionFile = "${Paths.jenkinsHome}/idea-plugin/currentPluginVersion"
        def oldVer = readFile versionFile
        def newVer = getPluginVersion()
        if (newVer != oldVer) {
            writeFile file: versionFile, text: newVer
            sh 'ant all'

            sh "cp -f lsfusion-idea-plugin.zip ${Paths.jenkinsHome}/installer-src/"

            sh 'sh upload-plugin.sh'

            slack.message "Plugin v.${getPluginVersion()} was built successfully.\n```${getReleaseNotes()}```"
        } else {
            echo "version's the same"
        }
    } catch (e) {
        slack.error "Warning! <$env.BUILD_URL|$currentBuild.fullDisplayName> failed."
        throw e
    }
}

@NonCPS
def getPluginVersion() {
    return new XmlSlurper().parse("${Paths.jenkinsHome}/idea-plugin/META-INF/plugin.xml").version.text()
}

@NonCPS
def getReleaseNotes() {
    def text = new File("${Paths.jenkinsHome}/idea-plugin/META-INF/plugin.xml").text
    def lis = new XmlSlurper().parseText(text.substring(text.indexOf("<ul>", 0), text.indexOf("</ul>", 0) + 5))
    def res = ""
    for (li in lis.children()) {
        res = "${res}• ${li.text()}\n"
    }
    return res
}