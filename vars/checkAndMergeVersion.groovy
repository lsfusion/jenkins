
import groovy.transform.Field
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def call(String branch, int branchIndex) {

    def skipBuild = false
    
    try {
        def cvFilePath = Paths.jenkinsHome + '/prevMergeAndDeployTest'
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
        }

        if (!skipBuild) {
            if (branchIndex >= 0)
                mergeVersion branchIndex

            prevBuilds[branch] = newBuildCommit
            cvFile.text = prevBuilds.inspect()
            
            return true
        }
        return false
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (branch " + branch + ") failed."
        
        throw e
    }
}

