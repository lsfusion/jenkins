def call(String branch) {
    update branch

    String platformVersion = readVersion()
    int majorVersion = platformVersion.replaceFirst(/\.[0-9]+-SNAPSHOT/, '') as Integer

//    buildWindowsInstallers(majorVersion, platformVersion)
//    buildRPMInstallers(majorVersion, platformVersion)
//    buildAPTInstallers(majorVersion, platformVersion)

    buildAndDeployDockerImages(platformVersion)
    sh "mkdir -p ${Paths.download}/docker/${platformVersion}"
    sh "cp -f compose.yaml ${Paths.download}/docker/${platformVersion}/"

    dir(Paths.download) {
        ftpPublisher failOnError: true, publishers: [
                [configName: 'Download FTP server',
                 transfers : [
//                         [sourceFiles: "exe/${platformVersion}/", remoteDirectory: "exe", flatten: true],
                                 [sourceFiles: "apt-snap/", remoteDirectory: "apt-snap", removePrefix: "apt-snap", cleanRemote: true],
//                                 [sourceFiles: "dnf-snap/", remoteDirectory: "dnf-snap", removePrefix: "dnf-snap", cleanRemote: true],
//                                 [sourceFiles: "docker/${tagVersion}/", remoteDirectory: "docker", removePrefix: "docker"]
                 ],
                 verbose   : true]
        ]
    }
}

