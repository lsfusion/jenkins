String lastVersionState
Integer lastVersion, lastSupportedVersion

// when there are enough features / every 3-6 months - private alfa feature release (no / rare features to be added), for inner testing / developing 
def call() {
//    pipeline {
//        agent any
//        stages {

    Integer masterVersion = null
    
    try {
            stage('Read last version') {
//                steps {
///                    script {
                        (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
//                    }
//                }
            }

            stage('Check last version state') {
//                steps {
//                    script {
                        if (!lastVersionState.equals("FINAL"))
                            error("Last version should be FINAL")
//                    }
//                }
            }
            stage('Update') {
//                steps {
                    update 'master'
//                }
            }
            stage('Release branch') {
//                steps {
//                        script {
                            masterVersion = lastVersion + 1
                            Integer nextMasterVersion = masterVersion + 1
//                        }
                withMaven {
                    sh "mvn release:clean release:branch -DbranchName=v$masterVersion -DdevelopmentVersion=$nextMasterVersion" + ".0-SNAPSHOT"
                }
//                }
            }

            stage('Deploy master') { // needed for update parents to work
    //                steps {
                if (checkAndMergeVersion('master', -1)) {
                    deploySnapshot('master', false)
                }
    //                }
            }
    
    
            if(!Paths.noCustomUpdates) {
                stage('Change custom parent versions') {
        //        steps {
                    withCredentials([usernameColonPassword(credentialsId: 'jenkins_lsfusion_org', variable: 'USERPASS')]) {
                        sh "curl -X POST 'http://jenkins.lsfusion.luxsoft.by/job/${Paths.updateParentVersionsJob}/build' --user ${USERPASS} -H 'Jenkins-Crumb:440561953171ba4497e4740562d172bb'"
                    }
        //        }
                }
            }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + masterVersion + ") failed."
    
        throw e
    }
    
    slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + masterVersion + ") succeeded."

//        }
//    }
}

