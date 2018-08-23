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

            // update version from a.beta.x TO a.0
            stage('Update version') {
//                steps {
//                    dir(Paths.src) {
                        sh "mvn release:clean release:update-versions -DdevelopmentVersion=$lastVersion.0-SNAPSHOT"
                        sh "mvn scm:checkin -Dmessage=\"Out of beta\""
//                    }
//                }
            }

            stage('Release fixes') {
//                steps {
                    releaseFixes() //build 'releaseFixes'
//                }
            }
//        }
//    }
}

