
def call(boolean releaseBeta, boolean releaseFinal) {
    String lastVersionState
    Integer lastVersion, lastSupportedVersion
    (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

    def lastPublicVersion = lastVersion - (lastVersionState.equals("ALPHA") && !releaseBeta ? 1 : 0)
    def branches = (lastSupportedVersion..lastPublicVersion).collect{it}
    for (branch in branches) {
        releaseVersion branch, releaseFinal
    }
}