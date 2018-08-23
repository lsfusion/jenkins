def call() {
    update "master"

    String masterVersion = readVersion()
    String[] masterVersionParts = masterVersion.split("\\.")
    
    int majorVersion = Integer.valueOf(masterVersionParts[0])
    if(masterVersionParts.size() != 3 || !masterVersionParts[1].equals("beta") || !masterVersionParts[2].equals("0-SNAPSHOT"))
        error("Version in master should be *.beta.0-SNAPSHOT")

    int lastVersion = majorVersion - 1
    int minorLastVersion
    boolean isLastBeta
    (isLastBeta, minorLastVersion) = getBranchVersion(lastVersion)
    
    if(isLastBeta) {
        if(minorLastVersion == 0)
            lastVersionState = "ALPHA"
        else 
            lastVersionState = "BETA"
    } else
        lastVersionState = "FINAL"
    
    int supportedVersions = 3
    int lastSupportedVersion = lastVersion - (supportedVersions - 1)
    if(lastSupportedVersion < 2)
        lastSupportedVersion = 2
    return [lastVersion, lastVersionState, lastSupportedVersion]
}
