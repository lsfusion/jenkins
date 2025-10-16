def call(String branch) {
    update branch
    
    def antlrDirPath = getResourcesDir() + Paths.antlr4;
    def workspace = Paths.src
    dir(antlrDirPath) {
        // Download ACE sources
        stage('get ace') {
            dir(antlrDirPath + '/ace') {
                git(
                        url: 'https://github.com/lsfusion/ace.git',
                        branch: 'master',
                )
            }
        }

        // Convert LsfLogics.g to LsfJSLogics.g4
        stage('create LsfJSLogics.g4') {
            sh 'chmod +x ./createLsfJSLogics_g4.sh'
            sh "./createLsfJSLogics_g4.sh ${workspace}/${Paths.lsfLogics}"
        }

        // Run antlr, generate LSFJSLogicsParser and LSFJSLogicsLexer and put all files into /webpack folder
        stage('run antlr') {
            sh 'java -cp antlr4-4.13.2-complete.jar org.antlr.v4.Tool -Dlanguage=JavaScript LsfJSLogics.g4 -o webpack'
        }

        // Download the webpack dependencies and run the build.
        stage('run npm') {
            // Target file antlrLsfJSLogics.js located in /dist directory
            dir(antlrDirPath + '/webpack') {
                sh 'npm install'
                sh 'npx webpack'
                sh "mkdir -p ${antlrDirPath}/ace/lib/ace/mode/antlr/"
                sh "cp ${antlrDirPath}/webpack/dist/antlrLsfJSLogics.js ${antlrDirPath}/ace/lib/ace/mode/antlr/"
            }
        }

        // Build ace. target directory /build.
        stage('building ace') {
            dir(antlrDirPath + '/ace') {
                sh 'npm install'
                sh 'node ./Makefile.dryice.js'
                sh "cp -f -r ${antlrDirPath}/ace/build/* ${workspace}/web-client/src/main/webapp/static/js/ace/"
            }
            sh "rm -r ${antlrDirPath}/ace"
        }
    }

//    sh "git add ${workspace}/web-client/src/main/webapp/static/js/ace/src/"
//    commit "Update ace"
//    push branch
}