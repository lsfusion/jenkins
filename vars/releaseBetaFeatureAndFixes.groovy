String lastVersionState
Integer lastVersion, lastSupportedVersion

// when works at least on all "test" production servers / in 2 months after alfa - public beta feature release, for outer testing / developing
def call() {
//    pipeline {
//        agent any
//        stages {
            stage('Read last version') {
        //                steps {
        ///                    script {
                (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
        //                    }
        //                }
            }
            stage('Check last version state') {
//                steps {
                    if(!lastVersionState.equals("ALPHA"))
                        error("Last version should be ALPHA")
//                }
            }

            stage('Release fixes') {
//                steps {
                    releaseVersions true
//                }
            }
//        }
//    }
}

