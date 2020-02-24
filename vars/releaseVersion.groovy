def call(int branch, boolean releaseFinal) {
    boolean isBeta
    boolean releaseBeta
    
    int majorVersion, minorVersion
    String tagVersion = "unknown"

    String changeLog = null

    try {
        stage('Get branch version info') {
//        steps {
            (isBeta, minorVersion) = getBranchVersion(branch)
            majorVersion = branch
            releaseBeta = isBeta && !releaseFinal
            if (isBeta && releaseFinal) {
                minorVersion = 0
            }
            tagVersion = majorVersion + '.' + (releaseBeta ? '0-beta' : '') + minorVersion
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
            if (releaseBeta) {
                releaseCommand += " -DdevelopmentVersion=$majorVersion.0-SNAPSHOT -DreleaseVersion=$tagVersion"
            }
            
            sh releaseCommand
            
            if (releaseBeta) {
                nextBetaVersion.set(minorVersion + 1)
            }
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
            buildInstallers majorVersion, tagVersion
            buildRPMInstallers majorVersion, tagVersion
            buildAPTInstallers majorVersion, tagVersion
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
            sh "mkdir -p ${Paths.download}/changelog"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:server:${tagVersion}:jar:assembly -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:server:${tagVersion}:jar:assembly-sources -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:desktop-client:${tagVersion}:jar:assembly -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:desktop-client:${tagVersion}:pack.gz:assembly -DoutputDirectory=${downloadDir}"
            sh "mvn dependency:copy -Dartifact=lsfusion.platform:web-client:${tagVersion}:war -DoutputDirectory=${downloadDir}"
            sh "cp -f CHANGELOG.md ${Paths.download}/changelog/CHANGELOG-${tagVersion}.txt"

            dir(downloadDir) {
                sh "mv -f server-${tagVersion}-assembly.jar lsfusion-server-${tagVersion}.jar"
                sh "mv -f server-${tagVersion}-assembly-sources.jar lsfusion-server-${tagVersion}-sources.jar"
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
                                 [sourceFiles: "${tagVersion}/", remoteDirectory: "java", flatten: true], 
                                 [sourceFiles: "changelog/CHANGELOG-${tagVersion}.txt", remoteDirectory: "changelog", flatten: true], 
                                 [sourceFiles: "exe/${tagVersion}/", remoteDirectory: "exe", flatten: true],
                                 [sourceFiles: "yum/", remoteDirectory: "yum", removePrefix: "yum"],
                                 [sourceFiles: "apt/", remoteDirectory: "apt", removePrefix: "apt"]
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
                    sh "curl -X POST 'http://jenkins.lsfusion.luxsoft.by/job/${Paths.updateAssembleVersionsJob}/build' --user ${USERPASS} -H 'Jenkins-Crumb:440561953171ba4497e4740562d172bb'"
                }
    //        }
            }
        }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + tagVersion + ") failed."
        
        throw e
    }

    slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + tagVersion + ") succeeded.\n```" + changeLog + "```"
}

