def call(String tagVersion, int majorVersion) {
    String clientName = "lsfusion$majorVersion-client"
    String serverName = "lsfusion$majorVersion-server"

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
                        client = docker.build("lsfusion/client:$tagVersion", "--build-arg APT_PACKAGE=$clientName --build-arg APT_VERSION=$tagVersion ./web-client") // eg. lsfusion/client:4.0-beta4
                        server = docker.build("lsfusion/server:$tagVersion", "--build-arg APT_PACKAGE=$serverName --build-arg APT_VERSION=$tagVersion ./server")
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