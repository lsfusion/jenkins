def call(String branch, boolean message) {
    update branch
    
    def lastCommitMessage = sh(returnStdout: true, script: "git log -n 1 --pretty=short")
    
    try {
        withMaven {
            sh "mvn -ntp clean deploy"
        }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (branch " + branch + ") failed."
        throw e
    }

    if (message) // master
        slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> succeeded.\n```" + lastCommitMessage + "```"
}
