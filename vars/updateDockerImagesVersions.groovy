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
                                filePath: 'compose.yaml'),
                        fileContentReplaceConfig(
                                configs: [
                                        fileContentReplaceItemConfig(
                                                search: "(lsfusion\\/client:).*",
                                                replace: "lsfusion/client:${tagVersion}",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'build/logics/docker-embed-server/compose.yaml'),
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
                                filePath: 'build/logics/docker/compose.yaml'),
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

        sh "mvn scm:checkin -Dmessage=\"Update docker images versions\""
    }
}