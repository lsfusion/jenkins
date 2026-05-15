def call(int branch, boolean releaseFinal) {
    boolean isBeta
    boolean releaseBeta
    boolean isLastVersion

    int majorVersion, minorVersion
    String tagVersion = "unknown"

    stage('Get branch version info') {
        (isBeta, minorVersion) = getBranchVersion(branch)
        majorVersion = branch
        releaseBeta = isBeta && !releaseFinal
        if (isBeta && releaseFinal) {
            minorVersion = 0
        }
        tagVersion = majorVersion + '.' + (releaseBeta ? '0-beta' : '') + minorVersion

        def (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
        isLastVersion = lastVersion == branch
    }

    stage('Update') {
        update "v$branch"
    }

    stage('Generate changelog') {
        generateChangeLog(tagVersion)
        sh "mvn scm:checkin -Dmessage=\"Generated change log\""
    }

    stage('Release branch') {
        String releaseCommand = "mvn -B release:clean release:prepare release:perform -P assemble -Dplatform.assemble.version=${tagVersion}"
        if (releaseBeta) {
            releaseCommand += " -DdevelopmentVersion=$majorVersion.0-SNAPSHOT -DreleaseVersion=$tagVersion"
        }

        sh releaseCommand

        if (releaseBeta) {
            nextBetaVersion.set(minorVersion + 1)
        }
    }

    stage('Update tag') {
        update.tag tagVersion
    }

    stage('Build and deploy docker images') {
        withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
            sh '''
              echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
            '''
            sh "mvn clean deploy -P assemble,docker,multiarch -Dplatform.assemble.version=${tagVersion}"
            if (isLastVersion && !releaseBeta) {
                // TODO: build & push 'latest' tag
                echo "Skipping 'latest' tag build (placeholder)"
            }
        }
    }

    stage('Upload to CDN') {
        dir("target") {
            def jarName = "mycompany-${tagVersion}.jar"
            sh "cp -fa lsfusion-server-${tagVersion}.jar ${jarName}"

            ftpPublisher failOnError: true, publishers: [
                    [configName: 'Download FTP server',
                     transfers : [
                             [sourceFiles: "${jarName}", remoteDirectory: "solutions"],
                             [sourceFiles: "compose.yaml", remoteDirectory: "solutions/mycompany-docker/${tagVersion}"]
                     ],
                     verbose   : true]
            ]
        }
    }
}
