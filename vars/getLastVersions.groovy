def call() {
    update "master"

    String masterVersion = readVersion()
    
    if (!(masterVersion =~ /[0-9]+\.0-SNAPSHOT/)) {
        error('Version in master should be *.0-SNAPSHOT')
    }

    int majorVersion = Integer.valueOf(masterVersion.substring(0, masterVersion.indexOf('.')))
    int lastVersion = majorVersion - 1
    int minorLastVersion
    boolean isLastBeta
    (isLastBeta, minorLastVersion) = getBranchVersion(lastVersion)
    
    String lastVersionState
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
