def call(int majorVersion, String platformVersion) {
    def isSnapshot = platformVersion.contains('-SNAPSHOT')
    def dnfSubdir = isSnapshot ? "dnf-snap" : "dnf"

    def downloadDir = "${Paths.download}/${dnfSubdir}"
    def remoteRedHat = "root@116.203.112.3"
    def remoteRpmFolder = "/usr/share/rpm"
    
    def rpmVersion = platformVersion
    def rpmRelease = '1' // though Release is optional, without it we get "error: Release field must be present in package"
    int currentRpmRelease = readLatestSnapshotRelease(platformVersion)
    if (platformVersion.contains('-beta')) {
        def betaIndex = platformVersion.indexOf('-beta')
        rpmVersion = platformVersion.substring(0, betaIndex)
        rpmRelease = platformVersion.substring(betaIndex + 1)
    } else if (isSnapshot) {
        rpmRelease = 'SNAPSHOT.' + currentRpmRelease
        rpmVersion = platformVersion.substring(0, platformVersion.indexOf('-SNAPSHOT'))
    }


    buildServerInstaller(majorVersion, platformVersion, rpmVersion, rpmRelease, remoteRedHat, remoteRpmFolder, dnfSubdir)
    buildClientInstaller(majorVersion, platformVersion, rpmVersion, rpmRelease, remoteRedHat, remoteRpmFolder, dnfSubdir)
    generateScripts(majorVersion, dnfSubdir)
    
    if (isSnapshot) {
        def keepSnapshotsNumber = 1
        def obsoleteRelease = currentRpmRelease - keepSnapshotsNumber
        if (obsoleteRelease > 0) {
            sh "ssh ${remoteRedHat} 'cd ${remoteRpmFolder}/${dnfSubdir}; rm -f lsfusion${majorVersion}-server-${platformVersion}.${obsoleteRelease}.noarch.rpm'"
            sh "ssh ${remoteRedHat} 'cd ${remoteRpmFolder}/${dnfSubdir}; rm -f lsfusion${majorVersion}-client-${platformVersion}.${obsoleteRelease}.noarch.rpm'"
            // metadata will be updated with 'createrepo' command
        }
        writeLatestSnapshotRelease(platformVersion, currentRpmRelease)
    }

    sh "ssh ${remoteRedHat} 'cd ${remoteRpmFolder}; createrepo --update ${dnfSubdir}'"
    sh "rm -rf ${Paths.rpm}/${dnfSubdir}"
    sh "scp -r ${remoteRedHat}:${remoteRpmFolder}/${dnfSubdir}/* ${Paths.rpm}/${dnfSubdir}/"
    sh "rm -rf ${downloadDir}"
    sh "mkdir -p ${downloadDir}"
//    sh "cp -fa ${Paths.rpm}/${dnfSubdir}/* ${downloadDir}/"
}

def buildServerInstaller(int majorVersion, String platformVersion, String rpmVersion, String rpmRelease, String remoteRedHat, String remoteRpmFolder, String dnfSubdir) {
    def title = "lsFusion $majorVersion Server"
    def serverName = "lsfusion$majorVersion-server"
    def templatesDir = getResourcesDir() + '/installer/dnf/server'

    dir(Paths.rpm) {
        sh 'rm -rf rpmbuild'
        sh 'mkdir rpmbuild'

        dir ('rpmbuild') {
            sh 'mkdir BUILD'
            sh 'mkdir RPMS'
            sh 'mkdir SOURCES'
            sh 'mkdir SPECS'
            sh 'mkdir SRPMS'
            
            sh "cp -fa $templatesDir/lsfusion.conf SOURCES/"
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-description>/$title/g' $templatesDir/lsfusion-server.service > SOURCES/${serverName}.service"
            sh "sed 's/<lsfusion-major-version>/$majorVersion/g; s/<lsfusion-version>/$rpmVersion/g; s/<lsfusion-release>/$rpmRelease/g; s/<lsfusion-title>/$title/g' $templatesDir/lsfusion.spec > SPECS/lsfusion.spec"
            
            sh "cp -fa $templatesDir/settings.properties SOURCES/"
            
            sh "mvn -f ${Paths.src}/pom.xml dependency:copy -Dartifact=lsfusion.platform:server:$platformVersion:jar:assembly -DoutputDirectory=${Paths.rpm}/rpmbuild/SOURCES/"
            sh "mv -f SOURCES/server-$platformVersion-assembly.jar SOURCES/server.jar"

            sh "ssh ${remoteRedHat} 'cd ${remoteRpmFolder}/rpmbuild; rm -rf *'"
            sh "scp -r * ${remoteRedHat}:${remoteRpmFolder}/rpmbuild"

            sh "ssh ${remoteRedHat} 'cd ${remoteRpmFolder}/rpmbuild; rpmbuild --buildroot `pwd`/BUILDROOT SPECS/lsfusion.spec -bb --define \"_topdir `pwd`\"'"
            sh "ssh ${remoteRedHat} 'expect ${remoteRpmFolder}/sign.exp' 2> /dev/null"

            sh "ssh ${remoteRedHat} 'cp -r ${remoteRpmFolder}/rpmbuild/RPMS/noarch/* ${remoteRpmFolder}/${dnfSubdir}/'"
        }
    }
}

def buildClientInstaller(int majorVersion, String platformVersion, String rpmVersion, String rpmRelease, String remoteRedHat, String remoteRpmFolder, String dnfSubdir) {
    def title = "lsFusion $majorVersion Client"
    def clientName = "lsfusion$majorVersion-client"
    def templatesDir = getResourcesDir() + '/installer/dnf/client'

    dir(Paths.rpm) {
        sh 'rm -rf rpmbuild'
        sh 'mkdir rpmbuild'

        dir ('rpmbuild') {
            sh 'mkdir BUILD'
            sh 'mkdir RPMS'
            sh 'mkdir SOURCES'
            sh 'mkdir SPECS'
            sh 'mkdir SRPMS'

            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion.conf > SOURCES/lsfusion.conf"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion.logrotate > SOURCES/lsfusion.logrotate"
            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-description>/$title/g' $templatesDir/lsfusion-client.service > SOURCES/${clientName}.service"
            sh "sed 's/<lsfusion-major-version>/$majorVersion/g; s/<lsfusion-version>/$rpmVersion/g; s/<lsfusion-release>/$rpmRelease/g; s/<lsfusion-title>/$title/g' $templatesDir/lsfusion.spec > SPECS/lsfusion.spec"

            sh 'cp -fa ../apache-tomcat-9.0.104.tar.gz SOURCES/'
            sh "cp -fa $templatesDir/ROOT.xml SOURCES/"

            sh "mvn -f ${Paths.src}/pom.xml dependency:copy -Dartifact=lsfusion.platform:web-client:$platformVersion:war -DoutputDirectory=${Paths.rpm}/rpmbuild/SOURCES/"
            sh "mv -f SOURCES/web-client-${platformVersion}.war SOURCES/client.war"

            sh "ssh ${remoteRedHat} 'cd ${remoteRpmFolder}/rpmbuild; rm -rf *'"
            sh "scp -r * ${remoteRedHat}:${remoteRpmFolder}/rpmbuild"
            
            sh "ssh ${remoteRedHat} 'cd ${remoteRpmFolder}/rpmbuild; rpmbuild --buildroot `pwd`/BUILDROOT SPECS/lsfusion.spec -bb --define \"_topdir `pwd`\"'"
            sh "ssh ${remoteRedHat} 'expect ${remoteRpmFolder}/sign.exp' 2> /dev/null"

            sh "ssh ${remoteRedHat} 'cp -r ${remoteRpmFolder}/rpmbuild/RPMS/noarch/* ${remoteRpmFolder}/${dnfSubdir}/'"
        }
    }
}

def generateScripts(int majorVersion, String dnfSubdir) {
    def templatesDir = getResourcesDir() + '/installer/dnf/scripts'
    def serverName = "lsfusion$majorVersion-server"
    def clientName = "lsfusion$majorVersion-client"

    dir(Paths.rpm) {
        sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-client>/$clientName/g; s/<repo_subdir>/$dnfSubdir/g' $templatesDir/install-lsfusion > ${dnfSubdir}/install-lsfusion$majorVersion"
    }
}

static def readLatestSnapshotRelease(String version) {
    File releasesFile = new File(Paths.rpm + '/latestSnapshotReleases');
    def prevReleases
    if (!releasesFile.exists())
        prevReleases = [:]
    else
        prevReleases = Eval.me(releasesFile.text)

    def prevRelease = prevReleases[version]
    return prevRelease == null ? 1 : (prevRelease as int) + 1
}

static def writeLatestSnapshotRelease(String version, int release) {
    File releasesFile = new File(Paths.rpm + '/latestSnapshotReleases');
    def prevReleases
    if (!releasesFile.exists())
        prevReleases = [:]
    else
        prevReleases = Eval.me(releasesFile.text)

    prevReleases[version] = release
    releasesFile.text = prevReleases.inspect()
}