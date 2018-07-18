def call(String platformVersion) {
    pipeline {
        agent any
        stages {
            stage('update') {
                steps {
                        sh "cp -fa ${Paths.src}/platform/web-api/src/main/resources/client.jnlp ${Paths.tmp}/client-${platformVersion}.jnlp"

                        sh """sed -i -e 's|\${jnlp.codebase}|http://download.lsfusion.org|' \\
-e "s/\$\"{jnlp.url}\"/client-${platformVersion}.jnlp/" \\
-e 's|\${jnlp.appName}|lsFusion|' \\
-e 's|\${jnlp.initHeapSize}|256m|' \\
-e 's|\${jnlp.maxHeapSize}|1024m|' \\
-e 's|\${jnlp.maxHeapFreeRatio}|70|' \\
-e 's|\${jnlp.vmargs}||' \\
-e 's|\${jnlp.registryHost}|localhost|' \\
-e 's|\${jnlp.registryPort}|7652|' \\
-e 's|\${jnlp.exportName}|default|' \\
-e 's|\${jnlp.singleInstance}|false|' \\
-e "s|lsfusion-client.jar|lsfusion-client-${platformVersion}.jar|" ${Paths.tmp}/client-${platformVersion}.jnlp"""

                    uploadToFtp "${Paths.tmp}/client-${platformVersion}.jnlp", "${platformVersion}"
//                    withCredentials([usernameColonPassword(credentialsId: 'lsfusion.ftp', variable: 'USERPASS')]) {
//                        sh "curl -T ${Paths.tmp}/client-${platformVersion}.jnlp --ftp-create-dirs ftp://ftp.keycdn.com/download/${platformVersion}/ --user $USERPASS"
//                    }

                    sh "rm -f ${Paths.tmp}/client-${platformVersion}.jnlp"
                }
            }
        }
    }
}

