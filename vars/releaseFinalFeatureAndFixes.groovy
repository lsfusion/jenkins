String lastVersionState
Integer lastVersion, lastSupportedVersion

// when there are no new bug reports / in 2 months after beta - public final release, for using in production
def call() {
    stage('Read last version') {
        (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
    }

    stage('Check last version state') {
        if (!lastVersionState.equals("BETA"))
            error("Last version should be BETA")
    }
    stage('Update') {
        update "v$lastVersion"
    }

    stage('Release final') {
        releaseVersions lastVersion, true
    }
}

