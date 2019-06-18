def call(int majorVersion, String platformVersion) {
    def downloadDir = "${Paths.download}/yum/${platformVersion}"
    
    buildServerInstaller(majorVersion, platformVersion)
    buildClientInstaller(majorVersion, platformVersion)

    dir(Paths.rpm) {
        sh 'createrepo --update yum'

        sh "mkdir -p ${downloadDir}"
        sh "cp -fa yum/* ${downloadDir}/"
    }
}

def buildServerInstaller(int majorVersion, String platformVersion) {
    def title = "lsFusion $majorVersion Server"
    def serverName = "lsfusion$majorVersion-server"
    def templatesDir = getResourcesDir() + '/installer/yum/server'

    dir(Paths.rpm) {
        sh 'rm -rf rpmbuild'
        sh 'mkdir rpmbuild'

        dir ('rpmbuild') {
            sh 'mkdir BUILD'
            sh 'mkdir RPMS'
            sh 'mkdir SOURCES'
            sh 'mkdir SPECS'
            sh 'mkdir SRPMS'
            
            sh "sed 's/<lsfusion-server>/$serverName/g' $templatesDir/lsfusion.conf > SOURCES/lsfusion.conf"
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-description>/$title/g' $templatesDir/lsfusion-server.service > SOURCES/${serverName}.service"
            sh "sed 's/<lsfusion-major-version>/$majorVersion/g; s/<lsfusion-version>/$platformVersion/g; s/<lsfusion-title>/$title/g' $templatesDir/lsfusion.spec > SPECS/lsfusion.spec"
            
            sh "cp -fa $templatesDir/settings.properties SOURCES/"
            
            sh "mvn -f ${Paths.src}/pom.xml dependency:copy -Dartifact=lsfusion.platform:server:$platformVersion:jar:assembly -DoutputDirectory=${Paths.rpm}/rpmbuild/SOURCES/"
            sh "mv -f SOURCES/server-$platformVersion-assembly.jar SOURCES/server.jar"

            sh '''#!/usr/bin/expect
set timeout -1
spawn bash -c {rpmbuild --buildroot `pwd`/BUILDROOT --sign SPECS/lsfusion.spec -bb --define \"_topdir `pwd`\"}
expect -exact "Enter pass phrase: "
send -- "f7y45cnb\r"
expect eof'''
            
            sh "cp -fa RPMS/noarch/* ${Paths.rpm}/yum/"
        }
    }
}

def buildClientInstaller(int majorVersion, String platformVersion) {
    def title = "lsFusion $majorVersion Client"
    def clientName = "lsfusion$majorVersion-client"
    def templatesDir = getResourcesDir() + '/installer/yum/client'

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
            sh "sed 's/<lsfusion-major-version>/$majorVersion/g; s/<lsfusion-version>/$platformVersion/g; s/<lsfusion-title>/$title/g' $templatesDir/lsfusion.spec > SPECS/lsfusion.spec"

            sh 'cp -fa ../apache-tomcat-9.0.20.tar.gz SOURCES/'
            sh "cp -fa $templatesDir/ROOT.xml SOURCES/"

            sh "mvn -f ${Paths.src}/pom.xml dependency:copy -Dartifact=lsfusion.platform:web-client:$platformVersion:war -DoutputDirectory=${Paths.rpm}/rpmbuild/SOURCES/"
            sh "mv -f SOURCES/web-client-${platformVersion}.war SOURCES/client.war"

            sh '''#!/usr/bin/expect
set timeout -1
spawn bash -c {rpmbuild --buildroot `pwd`/BUILDROOT --sign SPECS/lsfusion.spec -bb --define \"_topdir `pwd`\"}
expect -exact "Enter pass phrase: "
send -- "f7y45cnb\r"
expect eof'''
            
            sh "cp -fa RPMS/noarch/* ${Paths.rpm}/yum/"
        }
    }
}
