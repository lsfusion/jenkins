String lastVersionState
Integer lastVersion, lastSupportedVersion
(lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

// when works at least on all "test" production servers / in 2 months after alfa - public beta feature release, for outer testing / developing
def call() {
    pipeline {
        agent any
        stages {
            stage('Check last version state') {
                steps {
                    if(!lastVersionState.equals("ALPHA"))
                        error("Last version should be ALPHA")
                }
            }

            stage('Release fixes') {
                steps {
                    build 'releaseFixes'
                }
            }
        }
    }
}

