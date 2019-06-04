def call(int branch) {
    boolean isBeta
    int majorVersion, minorVersion
    String tagVersion = "unknown"

    String changeLog = null

    try {
        stage('Get branch version info') {
//        steps {
            (isBeta, minorVersion) = getBranchVersion(branch)
            majorVersion = branch
            tagVersion = majorVersion + '.' + (isBeta ? 'beta.' : '') + minorVersion
//        }
        }

        stage('Update') {
//        steps {
            update "v$branch"
//        }
        }

        stage('Generate changelog') {
//                steps {
//                    dir(Paths.src) {
            changeLog = generateChangeLog(tagVersion)
            sh "mvn scm:checkin -Dmessage=\"Generated change log\""
//                    }
//                }
        }

        stage('Release branch') {
//        steps {
            String releaseCommand = "mvn -B release:clean release:prepare release:perform"
            if (isBeta) {
                // release plugin automatically sets next version to x+1.beta.y-SNAPSHOT (not x.beta.y+1)
                String nextVersion = majorVersion + ".beta." + (minorVersion + 1) + "-SNAPSHOT"
                releaseCommand += " -DdevelopmentVersion=$nextVersion"
            }
            sh releaseCommand
//        }
        }

        // merging version changes
        stage('Fake merge version') {
//                steps {
//                    dir(Paths.src) {
            mergeVersion(branch, true)
//                    }
//                }
        }
    
        stage('Update tag') {
    //        steps {
            update.tag tagVersion
    //        }
        }
    //
    //    // Next 3 tasks to local folder
        stage('Build installers') {
    //        steps {
            buildInstallers tagVersion
    //        }
        }

        stage('Generate JNLP') {
    //        steps {
            generateJnlp tagVersion     
    //        }
        }
    
        stage('Copy dependencies') {
    //        steps {
            def downloadDir = "${Paths.download}/${tagVersion}"
            sh "mkdir -p ${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:server:${tagVersion}:jar:assembly -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:server:${tagVersion}:jar:sources -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:desktop-client:${tagVersion}:jar:assembly -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:desktop-client:${tagVersion}:pack.gz:assembly -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:web-client:${tagVersion}:war -DoutputDirectory=${downloadDir}"
            sh "cp -f CHANGELOG.md ${downloadDir}/CHANGELOG.txt"
    
            dir(downloadDir) {
                sh "mv -f server-${tagVersion}-assembly.jar lsfusion-server-${tagVersion}.jar"
                sh "mv -f server-${tagVersion}-sources.jar lsfusion-server-${tagVersion}-sources.jar"
                sh "mv -f desktop-client-${tagVersion}-assembly.jar lsfusion-client-${tagVersion}.jar"
                sh "mv -f desktop-client-${tagVersion}-assembly.pack.gz lsfusion-client-${tagVersion}.pack.gz"
                sh "mv -f web-client-${tagVersion}.war lsfusion-client-${tagVersion}.war"
            }
    //        }
        }

    //    // Upload from local folder to global
        stage('Upload to CDN') {
    //        steps {
            dir(Paths.download) {
                ftpPublisher failOnError: true, publishers: [
                        [configName: 'Download FTP server', 
                         transfers: [
                                 [sourceFiles: "${tagVersion}/"], 
                                 [sourceFiles: "exe/${tagVersion}/", remoteDirectory: "exe", flatten: true]
                         ], 
                         verbose: true]
                ]
            }          
    //        }
        }

    
        if(!Paths.noCustomUpdates) {
            stage('Change custom assemble versions') {
    //        steps {
                withCredentials([usernameColonPassword(credentialsId: 'jenkins_lsfusion_org', variable: 'USERPASS')]) {
                    sh "curl -X POST 'http://jenkins.lsfusion.luxsoft.by/job/updateAssembleVersions/build' --user ${USERPASS}"
                }
    //        }
            }
        }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + tagVersion + ") failed."
        
        throw e
    }

    slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + tagVersion + ") suceeded.\n```" + changeLog + "```"
}

