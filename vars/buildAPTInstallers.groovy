def call(int majorVersion, String platformVersion) {
    buildServerInstaller(majorVersion, platformVersion)
    buildClientInstaller(majorVersion, platformVersion)
    sh "ssh root@116.203.185.52 'cd /root/apt; reprepro -b repo/ includedeb all server/lsfusion$majorVersion-server_$platformVersion-1_all.deb; reprepro -b repo/ includedeb all client/lsfusion$majorVersion-client_$platformVersion-1_all.deb'"

    sh "mkdir -p ${Paths.download}/apt"
    
    generateScripts(majorVersion)
    
//    sh "scp -r root@116.203.185.52:/root/apt/repo/* ${Paths.download}/apt/"
}

def buildServerInstaller(int majorVersion, String platformVersion) {
    def title = "lsFusion $majorVersion Server"
    def serverName = "lsfusion$majorVersion-server"
    def templatesDir = getResourcesDir() + '/installer/apt/server'

    dir(Paths.apt) {
        sh 'rm -rf server'
        sh 'mkdir -p server/debbuild/debian'

        dir('server/debbuild') {
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-version>/$platformVersion/g' $templatesDir/changelog > debian/changelog"
//            new File("${Paths.apt}/server/debbuild/debian/changelog").append(new File("${Paths.src}/CHANGELOG.md").text)
            sh "cp -fa $templatesDir/compat debian/"
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-description>/$title/g' $templatesDir/control > debian/control"
            sh "cp -fa $templatesDir/lsfusion.conf ."
            sh "sed 's/<lsfusion-server>/$serverName/g' $templatesDir/lsfusion-server.postinst > debian/${serverName}.postinst"
            sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-description>/$title/g' $templatesDir/lsfusion-server.service > debian/${serverName}.service"
            sh "sed 's/<lsfusion-server>/$serverName/g' $templatesDir/rules > debian/rules"
            sh "cp -fa $templatesDir/settings.properties ."

            sh "mvn -f /usr/share/jenkins/src/pom.xml dependency:copy -Dartifact=lsfusion.platform:server:$platformVersion:jar:assembly -DoutputDirectory=${Paths.apt}/server/debbuild/"
            sh "mv -f server-$platformVersion-assembly.jar server.jar"

            sh "ssh root@116.203.185.52 'cd /root/apt/server; rm -rf *; mkdir debbuild'"

            sh 'scp -r * root@116.203.185.52:/root/apt/server/debbuild'
            
            withCredentials([usernamePassword(credentialsId: 'gpg_sign_key', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh "ssh root@116.203.185.52 'export GPG_TTY=\$(tty); cd /root/apt/server/debbuild; dpkg-buildpackage -kinfo@lsfusion.org -b -uc; debsign -p\"gpg --pinentry-mode loopback --passphrase ${PASSWORD}\"'"
            }
        }
    }
}

def buildClientInstaller(int majorVersion, String platformVersion) {
    def title = "lsFusion $majorVersion Client"
    def clientName = "lsfusion$majorVersion-client"
    def templatesDir = getResourcesDir() + '/installer/apt/client'

    dir(Paths.apt) {
        sh 'rm -rf client'
        sh 'mkdir -p client/debbuild/debian'

        dir('client/debbuild') {
            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-version>/$platformVersion/g' $templatesDir/changelog > debian/changelog"
//            new File("${Paths.apt}/client/debbuild/debian/changelog").append(new File("${Paths.src}/CHANGELOG.md").text)
            sh "cp -fa $templatesDir/compat debian/"
            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-description>/$title/g' $templatesDir/control > debian/control"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion.conf > lsfusion.conf"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion.logrotate > lsfusion.logrotate"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/lsfusion-client.postinst > debian/${clientName}.postinst"
            sh "sed 's/<lsfusion-client>/$clientName/g; s/<lsfusion-description>/$title/g' $templatesDir/lsfusion-client.service > debian/${clientName}.service"
            sh "cp -fa $templatesDir/ROOT.xml ./"
            sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/rules > debian/rules"

            sh "mvn -f /usr/share/jenkins/src/pom.xml dependency:copy -Dartifact=lsfusion.platform:web-client:$platformVersion:war -DoutputDirectory=${Paths.apt}/client/debbuild/"
            sh "mv -f web-client-${platformVersion}.war client.war"

            sh "ssh root@116.203.185.52 'cd /root/apt/client; rm -rf *; mkdir debbuild; cp -fa ../apache-tomcat-9.0.21.tar.gz debbuild/'"

            sh 'scp -r * root@116.203.185.52:/root/apt/client/debbuild/'

            withCredentials([usernamePassword(credentialsId: 'gpg_sign_key', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh "ssh root@116.203.185.52 'export GPG_TTY=\$(tty); cd /root/apt/client/debbuild; dpkg-buildpackage -kinfo@lsfusion.org -b -uc; debsign -p\"gpg --pinentry-mode loopback --passphrase ${PASSWORD}\"'"
            }
        }
    }
}

def generateScripts(int majorVersion) {
    def templatesDir = getResourcesDir() + '/installer/apt/scripts'
    def serverName = "lsfusion$majorVersion-server"
    def clientName = "lsfusion$majorVersion-client"

    dir(Paths.rpm) {
        sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-client>/$clientName/g' $templatesDir/install-lsfusion > ${Paths.download}/apt/install-lsfusion$majorVersion"
        sh "sed 's/<lsfusion-client>/$clientName/g' $templatesDir/install-lsfusion-client > ${Paths.download}/apt/install-$clientName"
        sh "cp -fa $templatesDir/install-lsfusion-db ${Paths.download}/apt/install-lsfusion$majorVersion-db"
        sh "sed 's/<lsfusion-server>/$serverName/g' $templatesDir/install-lsfusion-server > ${Paths.download}/apt/install-$serverName"
    }
}
