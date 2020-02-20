def call(int version) {
    update "v$version"

    String versionString = readVersion()

    if (!(versionString =~ /[0-9]+\.[0-9]+-SNAPSHOT/))
        error("Version in branch should be either *.*-SNAPSHOT")

    if (Integer.valueOf(versionString.substring(0, versionString.indexOf('.'))) != version)
        error("Major version in branch should be $version")

    boolean isBeta
    String minorVersion
    if (versionString =~ /[0-9]+\.0-SNAPSHOT/) {
        isBeta = true
        String betaVersion = latestBetaVersion.read()
        if (betaVersion =~ /[0-9]+/) { // ??
            minorVersion = Integer.valueOf(betaVersion)
        } else {
            minorVersion = 0
        }
    } else {
        isBeta = false
        minorVersion = Integer.valueOf(versionString.substring(versionString.indexOf('.') + 1, versionString.indexOf("-SNAPSHOT")))
    }

    return [isBeta, minorVersion]
}
