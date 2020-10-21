def call(String tagVersion, int majorVersion) {
    stage('Update dockerfiles') {
        String oldServerPackageWithVersion
        String oldServerPackage
        String oldClientPackageWithVersion
        String oldClientPackage
        String serverDockerfile = readFile "server/Dockerfile"
        String clientDockerfile = readFile "web-client/Dockerfile"
        (oldServerPackageWithVersion, oldServerPackage) = stringsForReplace(serverDockerfile)
        (oldClientPackageWithVersion, oldClientPackage) = stringsForReplace(clientDockerfile)

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
                                                search: oldServerPackageWithVersion,
                                                replace: "lsfusion$majorVersion-server=$tagVersion",
                                                matchCount: 0),
                                        fileContentReplaceItemConfig(
                                                search: oldServerPackage,
                                                replace: "lsfusion$majorVersion-server",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'server/Dockerfile'),
                        fileContentReplaceConfig(
                                configs: [
                                        fileContentReplaceItemConfig(
                                                search: oldClientPackageWithVersion,
                                                replace: "lsfusion$majorVersion-client=$tagVersion",
                                                matchCount: 0),
                                        fileContentReplaceItemConfig(
                                                search: oldClientPackage,
                                                replace: "lsfusion$majorVersion-client",
                                                matchCount: 0)
                                ],
                                fileEncoding: 'UTF-8',
                                filePath: 'web-client/Dockerfile')

                ])
        sh "mvn scm:checkin -Dmessage=\"Update docker images versions\""
    }
}

//get old version and apt package name from comment in first line of Dockerfile
static def stringsForReplace(String file){
    def packageNameWithVersion = file.split("\n")[0].split(" ")[1]
    def packageName = packageNameWithVersion.split("=")[0]
    return [packageNameWithVersion, packageName]
}