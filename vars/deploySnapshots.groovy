def call() {
    File latestCommitsBranchesFile = new File("${Paths.jenkinsHome}/latestCommitBranches");
    File latestCommitsMessagesFile = new File("${Paths.jenkinsHome}/latestCommitMessages");
    
    Set<String> branches = readLatestCommitBranches(latestCommitsBranchesFile)
    String commitMessages = readLatestCommitMessages(latestCommitsMessagesFile)
    boolean hasMaster = false 
    for (branch in branches) {
        if (branch == 'master') {
            hasMaster = true
        } else {
            deploySnapshot(branch, null, true, true)
            writeLatestDeployBranch(branch)
        }
    }
    
    // deploying master in the end
    if (hasMaster) {
        deploySnapshot('master', commitMessages, true, false)
        writeLatestDeployBranch('master') 
    }

    latestCommitsBranchesFile.delete()
    latestCommitsMessagesFile.delete()
}

def readLatestCommitBranches(File latestCommitBranchesFile) {
    if (!latestCommitBranchesFile.exists()) {
        return []
    } else {
        Set<String> latestBranches = new LinkedHashSet<>()
        def fileLines = latestCommitBranchesFile.readLines()
        for (String line : fileLines) {
            latestBranches.add(line.trim())
        }
        return latestBranches
    }
}

def writeLatestDeployBranch(String branch) {
    File branchesFile = new File("${Paths.jenkinsHome}/latestDeployBranches")
    Set<String> latestBranches = new LinkedHashSet<>()
    if (branchesFile.exists()) {
        def fileLines = branchesFile.readLines()
        for (String line : fileLines) {
            latestBranches.add(line.trim())
        }
    }

    latestBranches.add(branch)
    latestBranches = latestBranches.unique()
    def sortedSet = latestBranches.sort()
    branchesFile.write(sortedSet.join('\n'))
}

def readLatestCommitMessages(File latestCommitMessagesFile) {
    if (!latestCommitMessagesFile.exists()) {
        return ""
    } else {
        return latestCommitMessagesFile.text
    }
}
