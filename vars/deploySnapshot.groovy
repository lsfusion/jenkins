def call(String branch, String commitMessage, boolean uploadToCdn, boolean signDesktopJar) {
    update branch
    
    try {
        if (signDesktopJar) {
            sh "mvn -ntp clean deploy"
        } else {
            sh "mvn -ntp clean deploy -P-sign-desktop-jar"
        }
        
        if (uploadToCdn) {
            String platformVersion = readVersion()
            
            ftpPublisher failOnError: true, publishers: [
                    [configName: 'Download FTP server',
                     transfers : [
                             [sourceFiles: "server/target/lsfusion-server-${platformVersion}.jar," +
                                     "server/target/lsfusion-server-${platformVersion}-sources.jar," +
                                     "desktop-client/target/lsfusion-client-${platformVersion}.jar," + 
                                     "web-client/target/lsfusion-client-${platformVersion}.war", 
                              remoteDirectory: "java", 
                              flatten: true],
                     ],
                     verbose   : true]
            ]
        }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (branch " + branch + ") failed."
        throw e
    }

    if (commitMessage != null) // master
        slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> succeeded.\n```" + commitMessage + "```"
}
