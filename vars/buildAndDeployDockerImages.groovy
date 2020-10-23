def call(String tagVersion) {

    pipeline {
        environment {
            registryCredential = 'docker-hub'
            client = ''
            server = ''
        }

        agent any

        stages {
            stage('Building images') {
                steps {
                    script {
                        client = docker.build("lsfusion/client:$tagVersion", "--build-arg PACKAGE=${Paths.download}/${tagVersion}/lsfusion-client-${tagVersion}.war ./web-client") // eg. lsfusion/client:4.0-beta4
                        server = docker.build("lsfusion/server:$tagVersion", "--build-arg PACKAGE=${Paths.download}/${tagVersion}/lsfusion-server-${tagVersion}.jar ./server")
                    }
                }
            }

            stage('Deploy images') {
                steps {
                    script {
                        docker.withRegistry( '', registryCredential ) {
                            client.push()
                            server.push()
                        }
                    }
                }
            }

            stage('Cleaning up') {
                steps {
                    sh "docker rmi lsfusion/client:$tagVersion"
                    sh "docker rmi lsfusion/server:$tagVersion"
                }
            }
        }
    }
}