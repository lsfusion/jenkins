def call(String tagVersion) {
    stage('Update dockerfiles') {
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
                                                search: "(lsfusion-server).+?jar",
                                                replace: "lsfusion-server-${tagVersion}.jar",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'server/Dockerfile'),
                        fileContentReplaceConfig(
                                configs: [
                                        fileContentReplaceItemConfig(
                                                search: "(lsfusion-client).+?war",
                                                replace: "lsfusion-client-${tagVersion}.war",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'web-client/Dockerfile')

                ])

        withMaven {
            sh "mvn scm:checkin -Dmessage=\"Update docker images versions\""
        }
    }
}