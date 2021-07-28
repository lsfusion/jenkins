import groovy.json.JsonSlurper

def call() {
    try {
        git(
                url: 'git@github.com:lsfusion/js-client.git',
                branch: 'master',
        )

        def versionFile = "currentJSClientVersion"
        def oldVer = readFile versionFile
        def newVer = new JsonSlurper().parseText(new File("${Paths.jenkinsHome}/js-client/core/package.json").text).version
        if (newVer != oldVer) {
             dir("core") {
                 sh 'npm publish --access public'
             }

             slack.message "JS Client v.${newVer} was built successfully."

            writeFile file: versionFile, text: newVer
        } else {
            echo "version's the same"
        }
    } catch (e) {
         slack.error "Warning! <$env.BUILD_URL|$currentBuild.fullDisplayName> failed."
        throw e
    }
}
