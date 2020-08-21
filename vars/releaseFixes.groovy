// when there are enough bugfixes / every 2-4 weeks (except beta / final releases) - for using in production (later versions are more stable)
def call(int version) {
    int minorVersion
    boolean isBeta
    (isBeta, minorVersion) = getBranchVersion(version)

    if (isBeta) {
        error("Version should be FINAL")
    }

    releaseVersion version, false
}