def call(String branch, String commitMessage) {
    update branch
    
    try {
        sh "mvn -ntp clean deploy"
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (branch " + branch + ") failed."
        throw e
    }

    if (commitMessage != null) // master
        slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> succeeded.\n```" + commitMessage + "```"
}
