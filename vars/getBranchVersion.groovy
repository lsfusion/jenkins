def call(int version) {
    update "v$version"

    String versionString = readVersion()
    String[] versionParts = versionString.split("\\.")

    if ((versionParts.size() != 3 && versionParts.size() != 2) || !versionParts[versionParts.size() - 1].endsWith("-SNAPSHOT"))
        error("Version in branch should be either *.beta.*-SNAPSHOT or *.*-SNAPSHOT")

    if (Integer.valueOf(versionParts[0]) != version)
        error("Major version in branch should be $version.*.*-SNAPSHOT")

    boolean isBeta
    String minorVersionString
    if (versionParts.size() == 3) {
        if (!versionParts[1].equals("beta"))
            error("Beta version in branch should be *.beta.*-SNAPSHOT")

        isBeta = true
        minorVersionString = versionParts[2]
    } else {
        isBeta = false
        minorVersionString = versionParts[1]
    }

    return [isBeta, Integer.valueOf(minorVersionString.substring(0, minorVersionString.length() - "-SNAPSHOT".length()))]
}
