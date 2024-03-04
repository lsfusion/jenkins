def call(int branch, boolean fake = false) {

    String lastVersionState
    Integer lastVersion, lastSupportedVersion
    stage('Read last version') {
        (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
    }
    
    String nextBranch = branch >= lastVersion ? "master" : ("v" + (branch + 1))
    stage('Update') {
        update nextBranch
    }

    stage('Merge & Push') {
        sh "git merge" + (fake ? "  -s ours" : " -s recursive -Xignore-space-at-eol") + " origin/v$branch"
        push nextBranch
    }
}