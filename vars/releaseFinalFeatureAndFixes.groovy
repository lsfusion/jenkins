String lastVersionState
Integer lastVersion, lastSupportedVersion

// when there are no new bug reports / in 2 months after beta - public final release, for using in production
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
                    if(!lastVersionState.equals("BETA"))
                        error("Last version should be BETA")
//                }
            }
            stage('Update') {
//                steps {
                    update "v$lastVersion"
//                }
            }

            stage('Release fixes') {
//                steps {
                    releaseVersions false, true
//                }
            }
//        }
//    }
}

