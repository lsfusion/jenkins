def call() {
    update master

    String masterVersion = readVersion()
    String[] masterVersionParts = masterVersion.split("\\.")
    
    int majorVersion = Integer.valueOf(masterVersionParts[0])
    if(masterVersionParts.size() != 3 || !masterVersionParts[1].equals("beta") || !masterVersionParts[1].equals("beta"))
        error("Version in master should be *.beta.0-SNAPSHOT")
    
    update "v$majorVersion"
    
    String lastVersionString = readVersion()
    String[] lastVersionParts = masterVersion.split("\\.")
    int lastVersion = masterVersion - 1

    if((lastVersionParts.size() != 3 && lastVersionParts.size() != 2) || !lastVersionParts[lastVersionParts.size() - 1].endsWith("-SNAPSHOT"))
        error("Version in branch should be either *.beta.*-SNAPSHOT or *.*-SNAPSHOT")

    if(Integer.lastVersionParts[0]!=lastVersion)
        error("Major version in branch should be (masterVersion-1).*.*-SNAPSHOT")
    
    String lastVersionState
    if(lastVersionParts.size() == 3) {
        if(!lastVersionParts[1].equals("beta"))
            error("Beta version in branch should be *.beta.*-SNAPSHOT")

        if(lastVersionParts[2].substring(0, lastVersionParts[2].size() - "-SNAPSHOT".length()).equals("0")) {
            lastVersionState = "ALPHA"
        } else 
            lastVersionState = "BETA"
    } else
        lastVersionState = "FINAL"
    
    int supportedVersions = 1
    return [lastVersion, lastVersionState, lastVersion - (supportedVersions - 1)]
}
