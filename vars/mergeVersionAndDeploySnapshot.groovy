
import groovy.transform.Field
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def call(String branch, int branchIndex, boolean message) {

    def lastCommitMessage
    def skipBuild = false
    
    try {
        def cvFilePath = Paths.jenkinsHome + '/prevMergeAndDeploy'
        File cvFile = new File(cvFilePath);
        def prevBuilds
        if (!cvFile.exists())
            prevBuilds = [:]
        else
            prevBuilds = Eval.me(cvFile.text)

        def prevBuildCommit = prevBuilds[branch]
        def newBuildCommit

        stage('Check new changes') {
            update branch

            newBuildCommit = sh(returnStdout: true, script: "git rev-parse $branch")

            if (prevBuildCommit != null && prevBuildCommit.equals(newBuildCommit)) {
                echo "Nothing changed in $branch (current and previous commits are :$newBuildCommit). Skipping build..."
                skipBuild = true
            }

            lastCommitMessage = sh(returnStdout: true, script: "git log -n 1 --pretty=short")
        }

        if (!skipBuild) {
            if (branchIndex >= 0)
                mergeVersion branchIndex

            stage('Deploy') {
                deploySnapshot(branch)
            }

            prevBuilds[branch] = newBuildCommit
            cvFile.text = prevBuilds.inspect()
        }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (branch " + branch + ") failed."
        
        throw e
    }

    if(message && !skipBuild) // master
        slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> succeeded.\n```" + lastCommitMessage + "```"
}

