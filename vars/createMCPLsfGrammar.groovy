def call() {
    def antlrDirPath = getResourcesDir() + Paths.antlr4
    def workspace = Paths.src
    dir(antlrDirPath) {
        // Download MCP sources
        stage('get mcp') {
            dir(antlrDirPath + '/mcp') {
                git(
                        url: 'https://github.com/lsfusion/mcp.git',
                        branch: 'main',
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
            sh 'java -cp antlr4-4.13.2-complete.jar org.antlr.v4.Tool -Dlanguage=Python LsfJSLogics.g4 -o mcp/tools'
        }

        stage('push mcp') {
            dir(antlrDirPath + '/mcp') {
                sh '''
                    git add tools/
                    git commit -am "Update grammar"
                    git push origin main
                '''
            }
        }
    }
}