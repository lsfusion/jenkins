
def call(boolean forceAlphaRelease) {
    String lastVersionState
    Integer lastVersion, lastSupportedVersion
    (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

    def lastPublicVersion = lastVersion - (lastVersionState.equals("ALPHA") && !forceAlphaRelease ? 1 : 0)
    def branches = (lastSupportedVersion..lastPublicVersion).collect{it}
    for (branch in branches) {
        releaseVersion branch
    }
}