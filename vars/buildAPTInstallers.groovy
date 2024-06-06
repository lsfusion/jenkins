def call(int majorVersion, String platformVersion) {
    buildServerInstaller(majorVersion, platformVersion)
    buildClientInstaller(majorVersion, platformVersion)
    // reprepro stores only one latest version of the package. For some reason it refuses to remove previous version if it was in beta (e.g. when adding 3.0 after 3.beta.0). Therefore we delete packages manually.
    dir(Paths.apt) {
        sh "sudo sh -c 'reprepro -b repo remove all lsfusion$majorVersion-server; reprepro -b repo remove all lsfusion$majorVersion-client; reprepro -b repo/ includedeb all server/lsfusion$majorVersion-server_$platformVersion-1_all.deb; reprepro -b repo/ includedeb all client/lsfusion$majorVersion-client_$platformVersion-1_all.deb'"
    }

    sh "mkdir -p ${Paths.download}/apt"
    sh "rm -rf ${Paths.download}/apt/conf"
    sh "rm -rf ${Paths.download}/apt/db"
    sh "rm -rf ${Paths.download}/apt/dists"
    sh "rm -rf ${Paths.download}/apt/pool"
    
    generateScripts(majorVersion)

//    sh "cp -r ${Paths.apt}/repo/* ${Paths.download}/apt/"
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
            
            withCredentials([usernamePassword(credentialsId: 'gpg_sign_key', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh "sudo sh -c 'export GPG_TTY=\$(tty); dpkg-buildpackage -kinfo@lsfusion.org -b -uc; debsign -p\"gpg --pinentry-mode loopback --passphrase ${PASSWORD}\"'"
                sh "sudo sh -c 'chown -R jenkins .'"
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

            sh "cp -fa ../../apache-tomcat-9.0.89.tar.gz ."

            withCredentials([usernamePassword(credentialsId: 'gpg_sign_key', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh "sudo sh -c 'export GPG_TTY=\$(tty); dpkg-buildpackage -kinfo@lsfusion.org -b -uc; debsign -p\"gpg --pinentry-mode loopback --passphrase ${PASSWORD}\"'"
                sh "sudo sh -c 'chown -R jenkins .'"
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
