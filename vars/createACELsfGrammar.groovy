def call(String lsfLogicsPath) {
    def antlrDirPath = getResourcesDir() + Paths.antlr4;
    dir(antlrDirPath) {
        // Download ACE sources
        stage('get ace') {
            dir(antlrDirPath + '/ace') {
                git(
                        url: 'https://github.com/alex-byi/ace.git', // todo форкнуть и поменять репозиторий
                        branch: 'lsfusion',
                )
            }
        }

        // Convert LsfLogics.g to LsfJSLogics.g4
        stage('create LsfJSLogics.g4') {
            sh 'chmod +x ./createLsfJSLogics_g4.sh'
            sh "./createLsfJSLogics_g4.sh ${WORKSPACE}/${lsfLogicsPath}"
        }

        // Run antlr, generate LSFJSLogicsParser and LSFJSLogicsLexer and put all files into /webpack folder
        stage('run antlr') {
            sh 'java -cp antlr-4.9.2-complete.jar org.antlr.v4.Tool -Dlanguage=JavaScript LsfJSLogics.g4 -o webpack'
        }

        // Download the webpack dependencies and run the build.
        stage('run npm') {
            // Target file antlrLSFJSLogics.js located in /dist directory
            dir(antlrDirPath + '/webpack') {
                sh 'npm install'
                sh 'npx webpack'

                sh "cp ${antlrDirPath}/webpack/dist/antlrLsfJSLogics.js ${antlrDirPath}/ace/lib/ace/mode/antlr/"
            }
        }

        // Build ace. target directory /build.
        stage('building ace') {
            dir(antlrDirPath + '/ace') {
                sh 'npm install'
                sh 'node ./Makefile.dryice.js'
                sh "cp ${antlrDirPath}/ace/build/src/worker-lsf.js ${WORKSPACE}/web-client/src/main/webapp/static/js/ace/src"
            }
            sh "rm -r ${antlrDirPath}/ace"
        }
    }

    sh "git add -A"
    commit "Update worker-lsf.js"
    push "master"
}