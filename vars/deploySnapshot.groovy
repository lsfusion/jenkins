def call(String branch, String commitMessage, boolean uploadToCdn, boolean signDesktopJar) {
    update branch

    // the tests module runs its integration tests against a real postgres, which deploy goes through;
    // branches from before the module have no compose file, and are deployed exactly as they were
    boolean hasTests = fileExists('tests/compose.yaml')
    // the browser tests run in playwright's image, as the box has no browser; asked for only where they are, since
    // maven 3 only warns about a profile that is not there, and maven 4 fails on it. LSF_SKIP_UI_TESTS, a global
    // property of the jenkins, turns them off when what breaks is their stack - docker, npm, the image -, which
    // the quarantine of a test does not help with
    boolean hasUiTests = fileExists('tests/src/ui') && !env.LSF_SKIP_UI_TESTS

    try {
        // junit and archiveArtifacts below read whatever is on disk, and mvn clean only reaches the modules the build
        // gets to: cleared before anything here can fail, or a build that fails early reports the tests of the one before it
        sh "rm -rf */target/surefire-reports tests/target/failsafe-reports tests/target/ui"

        if (hasTests) {
            sh "docker compose -f tests/compose.yaml up -d db --wait"
        }

        // deployAtEnd : maven takes each module through deploy before starting the next, and tests is near the
        // end of the reactor - without it everything ahead of a failing test would already be published
        // maven.test.skip : the platform skips the unit tests of its modules unless asked for them; asked for only
        // where the tests module is, since the older branches carry some of the same tests still broken
        // gwt.localWorkers : GWT compiles the web client's permutations one after another unless told otherwise; half
        // of the box's cores, as each worker is a jvm of its own and the box has no swap
        String deploy = "mvn -ntp clean deploy -DdeployAtEnd=true -Dgwt.localWorkers=4" + (hasTests ? " -Dmaven.test.skip=false" : "") +
                (hasUiTests ? " -Pui -Dui.browser=docker" : "")
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
        // the logs of the browser tests' server and jetty, and a failed test's screenshot, page and trace
        archiveArtifacts allowEmptyArchive: true, artifacts: 'tests/target/ui/failures/**, tests/target/ui/*.log'
        if (hasTests) {
            sh "docker compose -f tests/compose.yaml down -v"
        }
    }

    if (commitMessage != null) // master
        slack.message "lsFusion SNAPSHOT artifacts published.\n<${env.BUILD_URL}|${currentBuild.fullDisplayName}>\n```" + commitMessage + "```"
}
