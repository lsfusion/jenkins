def call(String branch) {
    update branch

    String platformVersion = readVersion()
    int majorVersion = platformVersion.replaceFirst(/\.[0-9]+-SNAPSHOT/, '') as Integer

    buildWindowsInstallers(majorVersion, platformVersion)

    dir(Paths.download) {
        ftpPublisher failOnError: true, publishers: [
                [configName: 'Download FTP server',
                 transfers : [
                         [sourceFiles: "exe/${platformVersion}/", remoteDirectory: "exe", flatten: true],
//                                 [sourceFiles: "apt/", remoteDirectory: "apt", removePrefix: "apt"],
//                                 [sourceFiles: "dnf/", remoteDirectory: "dnf", removePrefix: "dnf"],
//                                 [sourceFiles: "docker/${tagVersion}/", remoteDirectory: "docker", removePrefix: "docker"]
                 ],
                 verbose   : true]
        ]
    }
}

