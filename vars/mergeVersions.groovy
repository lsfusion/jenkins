
def call() {
    String lastVersionState
    Integer lastVersion, lastSupportedVersion
    (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

    def branches = (lastSupportedVersion..lastVersion).collect{it}
    for (branch in branches) {
        mergeVersion branch
    }
}
