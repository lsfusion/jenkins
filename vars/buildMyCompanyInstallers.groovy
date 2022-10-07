def call(String platformVersion) {
    def myCompanyVersion
    def majorVersion = platformVersion.substring(0, platformVersion.indexOf('.'))
    def workspace = "${Paths.jenkinsHome}/installer"
    def myCompanyDir = "${Paths.jenkinsHome}/lsFusion/mycompany"
    def installerSrc = "${Paths.jenkinsHome}/installer-src"
    def installerBin = "${workspace}/install-bin"
    def resourcesDir = getResourcesDir()

    sh "rm -rf ${workspace}"
    sh "cp -r ${resourcesDir}/installer/exe/. ${workspace}"
    sh "chmod -R 777 ${workspace}"

    sh "cp -lr ${installerSrc}/* ${installerBin}"

    dir(myCompanyDir) {
        myCompanyVersion = sh script: 'mvn -N help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true
        myCompanyVersion = myCompanyVersion.replace('-SNAPSHOT', '')
    }

    dir(workspace) {
        def makensis = "${installerSrc}/nsis-unicode-win/makensis.exe"

        sh "echo '\n!define LSFUSION_MAJOR_VERSION ${majorVersion}' >> MyCompanyInstaller.nsh"
        sh "echo '\n!define LSFUSION_VERSION ${platformVersion.replace('-SNAPSHOT', '')}' >> MyCompanyInstaller.nsh"
        String viVersion = platformVersion.replace('-beta', '.999.').replace('-SNAPSHOT', '')
        if (!platformVersion.contains('beta')) {
            viVersion += '.0.0'
        }
        sh "echo '\n!define VI_LSFUSION_VERSION ${viVersion}' >> MyCompanyInstaller.nsh"
        sh "echo '\n!define MYCOMPANY_VERSION ${myCompanyVersion}' >> MyCompanyInstaller.nsh"
        sh "echo '\n!define DOWNLOAD_MYCOMPANY_JAR \"https://download.lsfusion.org/solutions/mycompany-${myCompanyVersion}.jar\"' >> MyCompanyInstaller.nsh"

        sh "wine ${makensis} MyCompany-installer-x64.nsi"
        sh "chmod -x MyCompany-${myCompanyVersion}-x64.exe"
        sh "sh ${Paths.ssl}/sign.sh ${workspace}/MyCompany-${myCompanyVersion}-x64.exe"

        ftpPublisher failOnError: true, publishers: [
                [configName: 'Download FTP server', 
                transfers: [
                        [sourceFiles: "MyCompany-${myCompanyVersion}-x64.exe", remoteDirectory: "solutions"]
                ], 
                verbose: true]
        ]
    }
}



