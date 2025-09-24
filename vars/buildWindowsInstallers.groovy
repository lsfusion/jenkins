def call(int majorVersion, String platformVersion) {
    def workspace = "${Paths.jenkinsHome}/installer"
    def installerSrc = "${Paths.jenkinsHome}/installer-src"
    def installerBin = "${workspace}/install-bin"
    def resourcesDir = getResourcesDir()

    sh "rm -rf ${workspace}"
    sh "cp -r ${resourcesDir}/installer/exe/. ${workspace}"
    sh "chmod -R 777 ${workspace}"

    sh "cp -lr ${installerSrc}/* ${installerBin}"

    sh "mvn dependency:copy -Dartifact=lsfusion.platform:server:${platformVersion}:jar:assembly -DoutputDirectory=${installerBin}"
    sh "mvn dependency:copy -Dartifact=lsfusion.platform:server:${platformVersion}:jar:assembly-sources -DoutputDirectory=${installerBin}"
    sh "mvn dependency:copy -Dartifact=lsfusion.platform:desktop-client:${platformVersion}:jar:assembly -DoutputDirectory=${installerBin}"
    sh "mvn dependency:copy -Dartifact=lsfusion.platform:web-client:${platformVersion}:war -DoutputDirectory=${installerBin}"

    dir(installerBin) {
        sh "mv -f server-${platformVersion}-assembly.jar server.jar"
        sh "mv -f server-${platformVersion}-assembly-sources.jar server-sources.jar"
        sh "mv -f desktop-client-${platformVersion}-assembly.jar client.jar"
        sh "mv -f web-client-${platformVersion}.war client.war"
    }

    dir(workspace) {
        def makensis = "${installerSrc}/nsis-unicode-win/makensis.exe"
        def downloadDir = "${Paths.download}/exe/${platformVersion}"

        sh "echo '\n!define LSFUSION_MAJOR_VERSION ${majorVersion}' >> Versions.nsh"
        sh "echo '\n!define LSFUSION_VERSION ${platformVersion.replace('-SNAPSHOT', '')}' >> Versions.nsh"
        sh "echo '\n!define LSFUSION_LIBRARIES_VERSION ${platformVersion}' >> Versions.nsh"
        String viVersion = platformVersion.replace('-beta', '.999.').replace('-SNAPSHOT', '.111.0')
        if (!platformVersion.contains('beta') && !platformVersion.contains('-SNAPSHOT')) {
            viVersion += '.0.0'
        }
        sh "echo '\n!define VI_LSFUSION_VERSION ${viVersion}' >> Versions.nsh"
        
        sh "mkdir -p ${downloadDir}"
        
        sh "wine ${makensis} Installer-x64.nsi"
        sh "chmod -x x64.exe"
//        sh "sh ${Paths.ssl}/sign.sh ${workspace}/x64.exe"
        sh "cp -f x64.exe ${downloadDir}/lsfusion-${platformVersion}-x64.exe"

        sh "wine ${makensis} Installer-x64-dev.nsi"
        sh "chmod -x x64-dev.exe"
//        sh "sh ${Paths.ssl}/sign.sh ${workspace}/x64-dev.exe"
        sh "cp -f x64-dev.exe ${downloadDir}/lsfusion-dev-${platformVersion}-x64.exe"

        sh "wine ${makensis} Installer-x64-desktop.nsi"
        sh "chmod -x x64-desktop.exe"
//        sh "sh ${Paths.ssl}/sign.sh ${workspace}/x64-desktop.exe"
        sh "cp -f x64-desktop.exe ${downloadDir}/lsfusion-desktop-${platformVersion}-x64.exe"
    }
}

