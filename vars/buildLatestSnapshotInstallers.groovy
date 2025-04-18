def call() {
    File latestDeployFile = new File("${Paths.jenkinsHome}/latestDeployBranches");

    Set<String> latestBranches = readLatestCommitBranches(latestDeployFile)
    for (branch in latestBranches) {
        buildSnapshotInstallers(branch)
    }

    latestDeployFile.delete()
}

def readLatestCommitBranches(File latestDeployFile) {
    if (!latestDeployFile.exists()) {
        return []
    } else {
        Set<String> latestBranches = Eval.me(latestDeployFile.text)
        return latestBranches
    }
}

