def call(int branch, boolean releaseFinal) {
    boolean isBeta
    int majorVersion, minorVersion
    String tagVersion = "unknown"

    stage('Get branch version info') {
        (isBeta, minorVersion) = getBranchVersion(branch)
        majorVersion = branch
        tagVersion = majorVersion + '.' + minorVersion
    }

    stage('Update') {
        update "v$branch"
    }

    stage('Generate changelog') {
        generateChangeLog(tagVersion)
        sh "mvn scm:checkin -Dmessage=\"Generated change log\""
    }

    stage('Release branch') {
        sh "mvn -B release:clean release:prepare release:perform -P assemble -Dplatform.assemble.version=${tagVersion}"
    }

    stage('Update tag') {
        update.tag tagVersion
    }

    stage('Build and deploy docker images') {
        // to generate Dockerfile
        sh "mvn clean install -P assemble,docker"

        buildAndDeployDockerImages(tagVersion, tagVersion)  
//        buildAndDeployDockerImages(tagVersion, 'latest')
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
