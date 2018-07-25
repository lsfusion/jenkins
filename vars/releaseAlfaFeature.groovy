String lastVersionState
Integer lastVersion, lastSupportedVersion
(lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()

// when there are enough features / every 3-6 months - private alfa feature release (no / rare features to be added), for inner testing / developing 
def call() {
    pipeline {
        agent any
        stages {
            stage('Check last version state') {
                steps {
                    if(!lastVersionState.equals("FINAL"))
                        error("Last version should be FINAL")
                }
            }
            stage('Update') {
                steps {
                    update 'master'
                }
            }
            stage('Release branch') {
                steps {
                    dir(Paths.src) {
                        def nextVersion = lastVersion + 1
                        sh "mvn release:clean release:branch -DbranchName=$lastVersion -DdevelopmentVersion=$nextVersion.beta.0-SNAPSHOT"
                    }
                }
            }
            
            stage('Change custom master versions') {
                steps {
                    // todo
                }
            }
        }
    }
}

