def call(int version) {
    update "v$version"

    String versionString = readVersion()

    if (!(versionString =~ /[0-9]+\.0-beta[0-9]+-SNAPSHOT/ || versionString =~ /[0-9]+\.[0-9]+-SNAPSHOT/))
        error("Version in branch should be either *.0-beta*-SNAPSHOT or *.*-SNAPSHOT")

    if (Integer.valueOf(versionString.substring(0, versionString.indexOf('.'))) != version)
        error("Major version in branch should be $version.*.*-SNAPSHOT")

    boolean isBeta
    String minorVersionString
    if (versionString =~ /[0-9]+\.0-beta[0-9]+-SNAPSHOT/) {
        isBeta = true
        minorVersionString = versionString.substring(versionString.indexOf('beta') + 4)
    } else {
        isBeta = false
        minorVersionString = versionString.substring(versionString.indexOf('.') + 1)
    }

    return [isBeta, Integer.valueOf(minorVersionString.substring(0, minorVersionString.indexOf("-SNAPSHOT")))]
}
