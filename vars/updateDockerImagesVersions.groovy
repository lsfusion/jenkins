def call(String tagVersion) {
    stage('Update dockerfiles') {
        String serverDockerfile = readFile "server/Dockerfile"
        String clientDockerfile = readFile "web-client/Dockerfile"

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
                                filePath: 'docker-compose.yml'),
                        fileContentReplaceConfig(
                                configs: [
                                        fileContentReplaceItemConfig(
                                                search: stringForReplace(serverDockerfile),
                                                replace: "lsfusion-server-${tagVersion}.jar",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'server/Dockerfile'),
                        fileContentReplaceConfig(
                                configs: [
                                        fileContentReplaceItemConfig(
                                                search: stringForReplace(clientDockerfile),
                                                replace: "lsfusion-client-${tagVersion}.war",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'web-client/Dockerfile')

                ])
        sh "mvn scm:checkin -Dmessage=\"Update docker images versions\""
    }
}

//get old package name from comment in first line of Dockerfile
static def stringForReplace(String file){
    return file.split("\n")[0].split(" ")[1]
}