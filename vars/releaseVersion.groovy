def call(int branch, boolean releaseFinal) {
    boolean isBeta
    int majorVersion, minorVersion
    String tagVersion = "unknown"
    String snapshotVersion = "unknown"
    String nextVersion = "unknown"

    stage('Get branch version info') {
        (isBeta, minorVersion) = getBranchVersion(branch)
        majorVersion = branch
        tagVersion = majorVersion + '.' + minorVersion
        snapshotVersion = "${majorVersion}.${minorVersion}-SNAPSHOT"
        nextVersion = "${majorVersion}.${minorVersion + 1}-SNAPSHOT"
    }

    stage('Update') {
        update "v$branch"
    }

//    stage('Generate changelog') {
//        generateChangeLog(tagVersion)
//        sh "mvn scm:checkin -Dmessage=\"Generated change log\""
//    }
//    
//    stage('Prepare release version') {
//        sh """
//            sed -i '/<parent>/,/<\\/parent>/ s|<version>${snapshotVersion}</version>|<version>${tagVersion}</version>|' pom.xml
//            sed -i 's|<platform.assemble.version>${snapshotVersion}</platform.assemble.version>|<platform.assemble.version>${tagVersion}</platform.assemble.version>|' pom.xml
//        """
//        sh "mvn scm:checkin -Dmessage=\"Preparing for release: platform v${tagVersion}\""
//    }

    stage('Release branch') {
        sh "mvn -B release:clean release:prepare release:perform -P assemble -Dplatform.assemble.version=${tagVersion}"
    }
    
    stage('Prepare next development version') {
        sh """
            sed -i '/<parent>/,/<\\/parent>/ s|<version>${tagVersion}</version>|<version>${nextVersion}</version>|' pom.xml
            sed -i 's|<platform.assemble.version>${tagVersion}</platform.assemble.version>|<platform.assemble.version>${nextVersion}</platform.assemble.version>|' pom.xml
        """
        sh "mvn scm:checkin -Dmessage=\"Platform v${nextVersion}\""
    }

    stage('Update tag') {
        update.tag tagVersion
    }

    stage('Build and deploy docker images') {
        // to generate Dockerfile
        sh "mvn clean install -P assemble,docker"

        buildAndDeployDockerImages(tagVersion, tagVersion)  
        buildAndDeployDockerImages(tagVersion, 'latest')
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
