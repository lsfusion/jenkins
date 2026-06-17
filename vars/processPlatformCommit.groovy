def call(boolean deployPlatform) {
    String lastVersionState
    Integer lastVersion, lastSupportedVersion
    (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
    
    int firstToDeploy = 0

    // Snapshot each branch's last-processed commit BEFORE the merge loop overwrites it
    // (checkAndMergeVersion rewrites prevMergeAndDeploy). Change-detection below diffs
    // prevCommit..HEAD instead of looking only at HEAD, so a platform commit still triggers a
    // deploy even when later docs/rag commits (or a skipped run) leave HEAD non-platform — the
    // old HEAD-only check silently dropped such commits and froze the snapshot.
    def prevCommits = readPrevBuildCommits()

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
        String deployBranch = firstToDeploy == -1 ? "master" : "v$firstToDeploy"
        update deployBranch
        String currentCommitMessage = sh(returnStdout: true, script: "git log -n 1 --pretty=short")

        // What changed since this branch was last processed (prevCommit..HEAD), per area.
        // prevMergeAndDeploy stores `git rev-parse` output (trailing newline) under plain String
        // keys — coerce the branch to String (GString keys would miss the map) and trim the commit
        // before interpolating it into a git range.
        String prevCommit = prevCommits[deployBranch]?.trim()
        boolean platformChanged = hasPathChanges(prevCommit, ["api", "build", "desktop-client", "server", "web-client"])
        boolean apiDesktopChanged = hasPathChanges(prevCommit, ["api", "desktop-client"])
        boolean lsfLogicsgChanged = hasPathChanges(prevCommit, [Paths.lsfLogics])
        boolean docsChanged = hasPathChanges(prevCommit, ["docs"])

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
            
            if (apiDesktopChanged) {
                if (firstToDeploy > 0) {
                    def deployBranches = (firstToDeploy..lastVersion).collect { it }
                    for (branch in deployBranches) {
                        removeSignedDesktopJar("v$branch")
                    }
                    // currently not signing SNAPSHOTS from master
                    //removeSignedDesktopJar("master")
                }
            }
            
            if (deployPlatform) {
                deploySnapshots()
            }
        }

        if (docsChanged) {
            ragIngestDocs(dryRun: false)
            deployDocumentation(currentCommitMessage)
        }
    }
}

def readPrevBuildCommits() {
    File cvFile = new File(Paths.jenkinsHome + '/prevMergeAndDeploy')
    return cvFile.exists() ? Eval.me(cvFile.text) : [:]
}

// True if any commit in (sinceCommit, HEAD] touched one of `paths`. A null marker (first run for
// the branch) counts as changed so the first build still deploys; an unrelated/rewritten
// sinceCommit errs toward "changed" (deploy) rather than silently skipping.
def hasPathChanges(String sinceCommit, List<String> paths) {
    if (sinceCommit == null) {
        return true
    }
    // If the marker commit isn't in the workspace (force-push / fresh clone / gc), a range diff
    // would fail with "bad revision"; treat that as changed and deploy rather than throw.
    if (sh(returnStatus: true, script: "git cat-file -e ${sinceCommit}^{commit}") != 0) {
        return true
    }
    String pathArgs = paths.collect { "'$it'" }.join(' ')
    String changed = sh(returnStdout: true,
            script: "git log ${sinceCommit}..HEAD --no-merges --full-history --pretty=format:%h -- ${pathArgs}").trim()
    return !changed.isEmpty()
}

def removeSignedDesktopJar(def branch) {
    update branch
    String platformVersion = readVersion()
    sh "rm -f ${Paths.signedDir}/lsfusion-client-${platformVersion}.jar"
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