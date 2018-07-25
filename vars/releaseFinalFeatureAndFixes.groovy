String lastVersionState
Integer lastVersion, lastSupportedVersion
(lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

// when there are no new bug reports / in 2 months after alpha - public final release, for using in production
def call() {
    pipeline {
        agent any
        stages {
            stage('Check last version state') {
                steps {
                    if(!lastVersionState.equals("BETA"))
                        error("Last version should be BETA")
                }
            }
            stage('Update') {
                steps {
                    update "$lastVersion"
                }
            }

            // update version from a.beta.x TO a.0
            stage('Update version') {
                steps {
                    dir(Paths.src) {
                        sh "mvn release:clean release:update-versions -DdevelopmentVersion=$lastVersion.0-SNAPSHOT"
                        sh "git commit -a -m \"Out of beta\""
                        sh "git push"
                    }
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

