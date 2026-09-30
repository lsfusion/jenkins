def call(String branch, String commitMessage, boolean uploadToCdn, boolean signDesktopJar) {
    update branch

    // the tests module runs its integration tests against a real postgres, which deploy goes through;
    // branches from before the module have no compose file, and are deployed exactly as they were
    boolean hasTests = fileExists('tests/compose.yaml')

    try {
        // junit below reads whatever reports are on disk, and mvn clean only reaches the modules the build gets to:
        // cleared before anything here can fail, or a build that fails early reports the tests of the one before it
        sh "rm -rf */target/surefire-reports tests/target/failsafe-reports"

        if (hasTests) {
            sh "docker compose -f tests/compose.yaml up -d db --wait"
        }

        // deployAtEnd : maven takes each module through deploy before starting the next, and tests is near the
        // end of the reactor - without it everything ahead of a failing test would already be published
        // maven.test.skip : the platform skips the unit tests of its modules unless asked for them; asked for only
        // where the tests module is, since the older branches carry some of the same tests still broken
        String deploy = "mvn -ntp clean deploy -DdeployAtEnd=true" + (hasTests ? " -Dmaven.test.skip=false" : "")
        if (signDesktopJar) {
            sh deploy
        } else {
            sh deploy + " -P-sign-desktop-jar"
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
        junit allowEmptyResults: true, testResults: '*/target/surefire-reports/*.xml, tests/target/failsafe-reports/*.xml' // what the rm above clears
        if (hasTests) {
            sh "docker compose -f tests/compose.yaml down -v"
        }
    }

    if (commitMessage != null) // master
        slack.message "lsFusion SNAPSHOT artifacts published.\n<${env.BUILD_URL}|${currentBuild.fullDisplayName}>\n```" + commitMessage + "```"
}
