def call(String branch, String commitMessage, boolean uploadToCdn) {
    update branch
    
    try {
//        sh "mvn -ntp clean deploy"
        print "deploySnapshot(branch, $commitMessage)"
        
//        if (uploadToCdn) {
//            String platformVersion = readVersion()
//            String shortVersion = platformVersion.replaceFirst(/\.[0-9]+-SNAPSHOT/, "-SNAPSHOT")
//            
//            sh "cp server/target/lsfusion-server-${platformVersion}.jar server/target/lsfusion-server-${shortVersion}.jar"
//            sh "cp server/target/lsfusion-server-${platformVersion}-sources.jar server/target/lsfusion-server-${shortVersion}-sources.jar"
//            sh "cp desktop-client/target/lsfusion-client-${platformVersion}.jar desktop-client/target/lsfusion-client-${shortVersion}.jar"
//            sh "cp web-client/target/lsfusion-client-${platformVersion}.war web-client/target/lsfusion-client-${shortVersion}.war"
//            
//            ftpPublisher failOnError: true, publishers: [
//                    [configName: 'Download FTP server',
//                     transfers : [
//                             [sourceFiles: "server/target/lsfusion-server-${shortVersion}.jar," +
//                                     "server/target/lsfusion-server-${shortVersion}-sources.jar," +
//                                     "desktop-client/target/lsfusion-client-${shortVersion}.jar," + 
//                                     "web-client/target/lsfusion-client-${shortVersion}.war", 
//                              remoteDirectory: "java", 
//                              flatten: true],
//                     ],
//                     verbose   : true]
//            ]
//        }
    } catch (e) {
        slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> (branch " + branch + ") failed."
        throw e
    }

    if (commitMessage != null) // master
        slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> succeeded.\n```" + commitMessage + "```"
}
