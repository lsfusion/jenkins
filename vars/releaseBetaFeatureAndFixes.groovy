String lastVersionState
Integer lastVersion, lastSupportedVersion

// when works at least on all "test" production servers / in 2 months after alfa - public beta feature release, for outer testing / developing
def call() {
    stage('Read last version') {
        (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
    }
    stage('Check last version state') {
        if (!lastVersionState.equals("ALPHA") && !lastVersionState.equals("BETA"))
            error("Last version should be ALPHA or BETA")
    }

    stage('Release beta') {
        releaseVersion lastVersion, false
    }
}

