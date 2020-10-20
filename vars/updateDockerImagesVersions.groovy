//need "content replace" jenkins plugin
def call(String tagVersion, int branch) {
    stage('change line') {
        contentReplace(
                configs: [
                        fileContentReplaceConfig(
                                configs: [
                                        fileContentReplaceItemConfig(
                                                search: "(lsfusion\\/server:).*",
                                                replace: "lsfusion/server:${tagVersion}",
                                                matchCount: 0),
                                        fileContentReplaceItemConfig(
                                                search: "(lsfusion\\/client:).*",
                                                replace: "lsfusion/client:${tagVersion}",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'docker-compose.yml')
                ])
        withCredentials([usernamePassword(credentialsId: Paths.githubCredentials, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
            sh 'git commit -am "Update docker images versions"'
            sh 'git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/' + Paths.githubRepo + '.git ' + branch + ":" + branch
        }
    }
}