def call(int branch, boolean releaseFinal) {
    boolean isBeta
    boolean releaseBeta

    int majorVersion, minorVersion
    String tagVersion = "unknown"

    String changeLog = null

    try {
        stage('Get branch version info') {
            (isBeta, minorVersion) = getBranchVersion(branch)
            majorVersion = branch
            releaseBeta = isBeta && !releaseFinal
            if (isBeta && releaseFinal) {
                minorVersion = 0
            }
            tagVersion = majorVersion + '.' + (releaseBeta ? '0-beta' : '') + minorVersion

            (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
            isLastVersion = lastVersion == branch
        }

        stage('Update') {
            update "v$branch"
        }

        stage('Generate changelog') {
            changeLog = generateChangeLog(tagVersion)
            sh "mvn scm:checkin -Dmessage=\"Generated change log\""
        }

        stage('Update dockerfiles') {
            if (releaseBeta || releaseFinal) {
                updateDockerImagesVersions tagVersion
            }
        }

        stage('Release branch') {
            String releaseCommand = "mvn -B release:clean release:prepare release:perform"
            if (releaseBeta) {
                releaseCommand += " -DdevelopmentVersion=$majorVersion.0-SNAPSHOT -DreleaseVersion=$tagVersion"
            }

            sh releaseCommand

            if (releaseBeta) {
                nextBetaVersion.set(minorVersion + 1)
            }
        }

        stage('Update dockerfiles') {
            String version = majorVersion + '.' + (releaseBeta ? 0 : minorVersion + 1) + '-SNAPSHOT'
            updateDockerImagesVersions version
        }

        // merging version changes
        stage('Fake merge version') {
            mergeVersion(branch, true)
        }

        stage('Update tag') {
            update.tag tagVersion
        }
        
    //    // Next 3 tasks to local folder
        stage('Build installers') {
            buildWindowsInstallers majorVersion, tagVersion
            buildRPMInstallers majorVersion, tagVersion
            buildAPTInstallers majorVersion, tagVersion
        }

        stage('Copy compose.yaml') {
            sh "mkdir -p ${Paths.download}/docker/${tagVersion}"
            sh "cp -f compose.yaml ${Paths.download}/docker/${tagVersion}/"
        }

        stage('Generate JNLP') {
            generateJnlp tagVersion
        }

        stage('Copy dependencies') {
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
        }

    //    // Upload from local folder to global
        stage('Upload to CDN') {
            dir(Paths.download) {
                ftpPublisher failOnError: true, publishers: [
                        [configName: 'Download FTP server',
                         transfers : [
                                 [sourceFiles: "${tagVersion}/", remoteDirectory: "java", flatten: true],
                                 [sourceFiles: "changelog/CHANGELOG-${tagVersion}.txt", remoteDirectory: "changelog", flatten: true],
                                 [sourceFiles: "exe/${tagVersion}/", remoteDirectory: "exe", flatten: true],
                                 [sourceFiles: "apt/", remoteDirectory: "apt", removePrefix: "apt"],
                                 [sourceFiles: "dnf/", remoteDirectory: "dnf", removePrefix: "dnf"],
                                 [sourceFiles: "docker/${tagVersion}/", remoteDirectory: "docker", removePrefix: "docker"]
                         ],
                         verbose   : true]
                ]
            }
        }

        stage('Build docker images') {
            buildAndDeployDockerImages tagVersion
            if (isLastVersion && !releaseBeta) {
                buildAndDeployDockerImages 'latest'
            }
        }

        if (!Paths.noCustomUpdates) {
            stage('Change custom assemble versions') {
                withCredentials([usernameColonPassword(credentialsId: 'jenkins_lsfusion_org', variable: 'USERPASS')]) {
                    sh "curl -X POST 'http://jenkins.lsfusion.luxsoft.by/job/${Paths.updateAssembleVersionsJob}/build' --user ${USERPASS} -H 'Jenkins-Crumb:440561953171ba4497e4740562d172bb'"
                }
            }
        }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + tagVersion + ") failed."

        throw e
    }

    slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> (v. " + tagVersion + ") succeeded.\n```" + changeLog + "```"
}

