def call() {
    String lastVersionState
    Integer lastVersion, lastSupportedVersion
    (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

    def branches = (lastSupportedVersion..lastVersion).collect{it}
    for (branch in branches) {
        mergeVersionAndDeploySnapshot "v$branch", branch, false
    }

    mergeVersionAndDeploySnapshot 'master', -1, true
}