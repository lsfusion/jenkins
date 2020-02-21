def call(int version) {
    update "v$version"

    String versionString = readVersion()

    if (!(versionString =~ /[0-9]+\.[0-9]+-SNAPSHOT/))
        error("Version in branch should be *.*-SNAPSHOT")

    if (Integer.valueOf(versionString.substring(0, versionString.indexOf('.'))) != version)
        error("Major version in branch should be $version")

    boolean isBeta
    Integer minorVersion
    if (versionString =~ /[0-9]+\.0-SNAPSHOT/) {
        isBeta = true
        String betaVersion = nextBetaVersion.get()
        println "beta: " + betaVersion
        if (betaVersion =~ /[0-9]+/) { // ??
            minorVersion = Integer.valueOf(betaVersion)
            println "betaVersion: " + minorVersion
        } else {
            minorVersion = 0
        }
    } else {
        isBeta = false
        minorVersion = Integer.valueOf(versionString.substring(versionString.indexOf('.') + 1, versionString.indexOf("-SNAPSHOT")))
    }
    println 'getBranchVersion: ' + isBeta + ', ' + minorVersion
    return [isBeta, minorVersion]
}
