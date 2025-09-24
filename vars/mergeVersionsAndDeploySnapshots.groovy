def call() {
    String lastVersionState
    Integer lastVersion, lastSupportedVersion
    (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
    
    int firstToDeploy = 0

    def branches = (lastSupportedVersion..lastVersion).collect{it}
    for (branch in branches) {
        if (checkAndMergeVersion("v$branch", branch) && firstToDeploy == 0) {
            firstToDeploy = branch
        }
    }
    if (checkAndMergeVersion('master', -1) && firstToDeploy == 0) {
        firstToDeploy = -1
    }

    if (firstToDeploy != 0) {
        update firstToDeploy == -1 ? "master" : "v$firstToDeploy"
        def currentCommit = sh(returnStdout: true, script: "git log -n 1 --no-merges --pretty=format:\"%h\"")
        String currentCommitMessage = sh(returnStdout: true, script: "git log -n 1 --pretty=short")
        boolean platformChanged = platformChanged(currentCommit)
        boolean lsfLogicsgChanged = lsfLogicsgChanged(currentCommit)
        boolean docsChanged = docsChanged(currentCommit)

        if (lsfLogicsgChanged) {
            if (firstToDeploy > 0) {
                def deployBranches = (firstToDeploy..lastVersion).collect { it }
                for (branch in deployBranches) {
                    createACELsfGrammar("v$branch")
                    checkAndMergeVersion("v$branch", branch, true) // fake merge as regular merge is impossible
                }
            }
            
            createACELsfGrammar("master")
            checkAndMergeVersion("master", -1, true)
        }

        if (platformChanged) {
            Set<String> branchesSet = new LinkedHashSet<>()
            if (firstToDeploy > 0) {
                def deployBranches = (firstToDeploy..lastVersion).collect { it }
                for (branch in deployBranches) {
                    branchesSet.add("v$branch")
                }
            }
            branchesSet.add("master")
            
            writeLatestCommitBranches(branchesSet)
            writeLatestCommitMessage(currentCommitMessage)
            
//            deploySnapshots()
        }

        if (docsChanged) {
            deployDocumentation(currentCommitMessage)
        }
    }
}

def lsfLogicsgChanged(def currentCommit) {
    return currentCommit == readLatestCommit(Paths.lsfLogics)
}

def docsChanged(def currentCommit) {
    return currentCommit == readLatestCommit("docs")
}

def platformChanged(def currentCommit) {
    return currentCommit == readLatestCommit("api") ||
            currentCommit == readLatestCommit("build") ||
            currentCommit == readLatestCommit("desktop-client") ||
            currentCommit == readLatestCommit("server") ||
            currentCommit == readLatestCommit("web-client")
}

def readLatestCommit(dir) {
    return sh(returnStdout: true, script: "git log -n 1 --no-merges --pretty=format:\"%h\" --full-history -- $dir")
}

def writeLatestCommitBranches(Set<String> branches) {
    File branchesFile = new File("${Paths.jenkinsHome}/latestCommitBranches")
    Set<String> latestBranches = new LinkedHashSet<>()
    if (branchesFile.exists()) {
        def fileLines = branchesFile.readLines()
        for (String line : fileLines) {
            latestBranches.add(line.trim())
        }
    }
    
    latestBranches.addAll(branches)
    latestBranches = latestBranches.unique()
    def sortedSet = latestBranches.sort()

    branchesFile.write(sortedSet.join('\n'))
}

def writeLatestCommitMessage(String message) {
    File commitsFile = new File("${Paths.jenkinsHome}/latestCommitMessages")
    String allMessages = ""
    if (commitsFile.exists()) {
        allMessages = commitsFile.text + "\n"
    }
    allMessages += message

    commitsFile.text = allMessages
}