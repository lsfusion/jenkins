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

    def lsfLogicsPath = lsfLogicsgChanged()
    if (lsfLogicsPath) {
        createACELsfGrammar(lsfLogicsPath)
    }

    if (checkAndMergeVersion('master', -1) && firstToDeploy == 0) {
        firstToDeploy = -1
    }

    if (platformChanged()) {
        if (firstToDeploy > 0) {
            def deployBranches = (firstToDeploy..lastVersion).collect{it}
            for (branch in deployBranches) {
                deploySnapshot("v$branch", false)
            }
        }
        if (firstToDeploy != 0) {
            deploySnapshot("master", true)
        }
    }

    if (docsChanged()) {
        deployDocusaurus()
    }
}

def lsfLogicsgChanged() {
    update 'master'
    def changeSet = currentBuild.rawBuild.changeSets
    for (int i = 0; i < changeSet.size(); i++) {
        def items = changeSet[i].items
        for (int j = 0; j < items.size(); j++) {
            def files = items[j].affectedFiles
            for (int k = 0; k < files.size(); k++) {
                if (files[k].path.contains("lsfusion/server/language/LsfLogics.g")) {
                    return files[k].path
                }
            }
        }
    }
}

def docsChanged() {
    update 'master'
    def rootCommit = sh returnStdout: true, script: "git log -n 1 --no-merges --pretty=format:\"%h\""
    return rootCommit == readLatestCommit("docs")
}

def platformChanged() {
    update 'master'
    def rootCommit = sh returnStdout: true, script: "git log -n 1 --no-merges --pretty=format:\"%h\""
    return rootCommit == readLatestCommit("api") || 
            rootCommit == readLatestCommit("build") || 
            rootCommit == readLatestCommit("desktop-client") || 
            rootCommit == readLatestCommit("server") || 
            rootCommit == readLatestCommit("web-client")
}

def readLatestCommit(dir) {
    return sh(returnStdout: true, script: "git log -n 1 --no-merges --pretty=format:\"%h\" --full-history -- $dir")
}