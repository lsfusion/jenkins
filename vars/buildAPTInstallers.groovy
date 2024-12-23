def call(int majorVersion, String platformVersion) {
    def isSnapshot = platformVersion.contains('-SNAPSHOT')
    def aptSubdir = isSnapshot ? "apt-snap" : "apt"
    def downloadDir = "${Paths.download}/${aptSubdir}"
    def repoSubdir = isSnapshot ? "repo-snap" : "repo"
    int currentAptRelease = 1
    def aptVersion

    if (isSnapshot) {
        currentAptRelease = readLatestSnapshotRelease(platformVersion)
    }
    
    aptVersion = platformVersion + '-' + currentAptRelease
    
    buildServerInstaller(majorVersion, platformVersion, aptVersion)
    buildClientInstaller(majorVersion, platformVersion, aptVersion)

//    if (isSnapshot) {
//        def keepSnapshotsNumber = 5
//        def obsoleteRelease = currentAptRelease - keepSnapshotsNumber
//        if (obsoleteRelease > 0) {
//            sh "sudo sh -c 'reprepro -b $repoSubdir remove all lsfusion$majorVersion-server; reprepro -b $repoSubdir remove all lsfusion$majorVersion-client'"
////            lsfusion${majorVersion}-server-${platformVersion}.${obsoleteRelease}.noarch.rpm
////            lsfusion${majorVersion}-client-${platformVersion}.${obsoleteRelease}.noarch.rpm
//        }
//    }
    
    // reprepro stores only one latest version of the package. For some reason it refuses to remove previous version if it was in beta (e.g. when adding 3.0 after 3.beta.0). Therefore we delete packages manually.
    dir(Paths.apt) {
        if (isSnapshot) {
            sh "sudo reprepro -b $repoSubdir process_incoming default"
        } else {
            sh "sudo sh -c 'reprepro -vv -b $repoSubdir includedeb all server/lsfusion$majorVersion-server_${aptVersion}_all.deb; reprepro -vv -b $repoSubdir includedeb all client/lsfusion$majorVersion-client_${aptVersion}_all.deb'"
//        sh "sudo sh -c 'reprepro -b $repoSubdir remove all lsfusion$majorVersion-server; reprepro -b $repoSubdir remove all lsfusion$majorVersion-client; reprepro -b $repoSubdir/ includedeb all server/lsfusion$majorVersion-server_${aptVersion}_all.deb; reprepro -b $repoSubdir/ includedeb all client/lsfusion$majorVersion-client_${aptVersion}_all.deb'"
        }
    }

    if (isSnapshot) {
        writeLatestSnapshotRelease(platformVersion, currentAptRelease)
    }

    sh "mkdir -p ${downloadDir}"
    sh "rm -rf ${downloadDir}/conf"
    sh "rm -rf ${downloadDir}/db"
    sh "rm -rf ${downloadDir}/dists"
    sh "rm -rf ${downloadDir}/pool"
    
    generateScripts(majorVersion, downloadDir, aptSubdir)

    sh "cp -r ${Paths.apt}/${repoSubdir}/* ${downloadDir}/"
}

def buildServerInstaller(int majorVersion, String platformVersion, String aptVersion) {
    def title = "lsFusion $majorVersion Server"
    def serverName = "lsfusion$majorVersion-server"
    def templatesDir = getResourcesDir() + '/installer/apt/server'

    dir(Paths.apt) {
        sh 'rm -rf server'
        sh 'mkdir -p server/debbuild/debian'

        dir('server/debbuild') {
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-version>/$aptVersion/g' $templatesDir/changelog > debian/changelog"
//            new File("${Paths.apt}/server/debbuild/debian/changelog").append(new File("${Paths.src}/CHANGELOG.md").text)
            sh "cp -fa $templatesDir/compat debian/"
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-description>/$title/g' $templatesDir/control > debian/control"
//            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-description>/$title/g; s/<lsfusion-version>/$aptVersion/g' $templatesDir/control > debian/control"
            sh "cp -fa $templatesDir/lsfusion.conf ."
            sh "sed 's/<lsfusion-server>/$serverName/g' $templatesDir/lsfusion-server.postinst > debian/${serverName}.postinst"
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-description>/$title/g' $templatesDir/lsfusion-server.service > debian/${serverName}.service"
            sh "sed 's/<lsfusion-server>/$serverName/g' $templatesDir/rules > debian/rules"
            sh "cp -fa $templatesDir/settings.properties ."

            sh "mvn -f /usr/share/jenkins/src/pom.xml dependency:copy -Dartifact=lsfusion.platform:server:$platformVersion:jar:assembly -DoutputDirectory=${Paths.apt}/server/debbuild/"
            sh "mv -f server-$platformVersion-assembly.jar server.jar"
            
            withCredentials([usernamePassword(credentialsId: 'gpg_sign_key', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh "sudo sh -c 'export GPG_TTY=\$(tty); dpkg-buildpackage -kinfo@lsfusion.org -b -uc; debsign -p\"gpg --pinentry-mode loopback --passphrase ${PASSWORD}\"'"
                sh "sudo sh -c 'chown -R jenkins .'"
            }
        }
        
        sh "cp -fa server/lsfusion$majorVersion-server_${aptVersion}_all.deb incoming/"
    }
}

def buildClientInstaller(int majorVersion, String platformVersion, String aptVersion) {
    def title = "lsFusion $majorVersion Client"
    def clientName = "lsfusion$majorVersion-client"
    def templatesDir = getResourcesDir() + '/installer/apt/client'

    dir(Paths.apt) {
        sh 'rm -rf client'
        sh 'mkdir -p client/debbuild/debian'

        dir('client/debbuild') {
            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-version>/$aptVersion/g' $templatesDir/changelog > debian/changelog"
//            new File("${Paths.apt}/client/debbuild/debian/changelog").append(new File("${Paths.src}/CHANGELOG.md").text)
            sh "cp -fa $templatesDir/compat debian/"
            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-description>/$title/g' $templatesDir/control > debian/control"
//            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-description>/$title/g; s/<lsfusion-version>/$aptVersion/g' $templatesDir/control > debian/control"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion.conf > lsfusion.conf"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion.logrotate > lsfusion.logrotate"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion-client.postinst > debian/${clientName}.postinst"
            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-description>/$title/g' $templatesDir/lsfusion-client.service > debian/${clientName}.service"
            sh "cp -fa $templatesDir/ROOT.xml ./"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/rules > debian/rules"

            sh "mvn -f /usr/share/jenkins/src/pom.xml dependency:copy -Dartifact=lsfusion.platform:web-client:$platformVersion:war -DoutputDirectory=${Paths.apt}/client/debbuild/"
            sh "mv -f web-client-${platformVersion}.war client.war"

            sh "cp -fa ../../apache-tomcat-9.0.89.tar.gz ."

            withCredentials([usernamePassword(credentialsId: 'gpg_sign_key', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh "sudo sh -c 'export GPG_TTY=\$(tty); dpkg-buildpackage -kinfo@lsfusion.org -b -uc; debsign -p\"gpg --pinentry-mode loopback --passphrase ${PASSWORD}\"'"
                sh "sudo sh -c 'chown -R jenkins .'"
            }
        }

        sh "cp -fa server/lsfusion$majorVersion-client_${aptVersion}_all.deb incoming/"
    }
}

def generateScripts(int majorVersion, def downloadDir, def aptSubdir) {
    def templatesDir = getResourcesDir() + '/installer/apt/scripts'
    def serverName = "lsfusion$majorVersion-server"
    def clientName = "lsfusion$majorVersion-client"

    dir(Paths.apt) {
        sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-client>/$clientName/g; s/<repo_subdir>/$aptSubdir/g' $templatesDir/install-lsfusion > ${downloadDir}/install-lsfusion$majorVersion"
        sh "sed 's/<lsfusion-client>/$clientName/g; s/<repo_subdir>/$aptSubdir/g' $templatesDir/install-lsfusion-client > ${downloadDir}/install-$clientName"
        sh "cp -fa $templatesDir/install-lsfusion-db ${downloadDir}/install-lsfusion$majorVersion-db"
        sh "sed 's/<lsfusion-server>/$serverName/g; s/<repo_subdir>/$aptSubdir/g' $templatesDir/install-lsfusion-server > ${downloadDir}/install-$serverName"
    }
}

static def readLatestSnapshotRelease(String version) {
    File releasesFile = new File(Paths.apt + '/latestSnapshotReleases');
    def prevReleases
    if (!releasesFile.exists())
        prevReleases = [:]
    else
        prevReleases = Eval.me(releasesFile.text)

    def prevRelease = prevReleases[version]
    return prevRelease == null ? 1 : (prevRelease as int) + 1
}

static def writeLatestSnapshotRelease(String version, int release) {
    File releasesFile = new File(Paths.apt + '/latestSnapshotReleases');
    def prevReleases
    if (!releasesFile.exists())
        prevReleases = [:]
    else
        prevReleases = Eval.me(releasesFile.text)

    prevReleases[version] = release
    releasesFile.text = prevReleases.inspect()
}