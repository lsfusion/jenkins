def call(String branch, String commitMessage, boolean uploadToCdn, boolean signDesktopJar) {
    update branch

    // the tests module runs its integration tests against a real postgres, which deploy goes through;
    // branches from before the module have no compose file, and are deployed exactly as they were
    boolean hasTests = fileExists('tests/compose.yaml')

    try {
        if (hasTests) {
            sh "docker compose -f tests/compose.yaml up -d db --wait"
        }

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
    } finally {
        junit allowEmptyResults: true, testResults: 'tests/target/failsafe-reports/*.xml, **/target/surefire-reports/*.xml'
        if (hasTests) {
            sh "docker compose -f tests/compose.yaml down -v"
        }
    }

    if (commitMessage != null) // master
        slack.message "lsFusion SNAPSHOT artifacts published.\n<${env.BUILD_URL}|${currentBuild.fullDisplayName}>\n```" + commitMessage + "```"
}
