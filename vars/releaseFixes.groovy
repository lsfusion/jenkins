String lastVersionState
Integer lastVersion, lastSupportedVersion
(lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

// when there are enough bugfixes / every 2-4 weeks (except beta / final releases) - for using in production (later versions are more stable)
def call() {
        def branches = (lastSupportedVersion..(lastVersion - (lastVersionState.equals("ALPHA")?1:0))).collect{it}
        for (branch in branches) {
                releaseFix "$branch"
        }
}