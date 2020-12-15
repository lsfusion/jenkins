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
                        client = docker.build("lsfusion/client:$tagVersion", "./web-client") // eg. lsfusion/client:4.0-beta4
                        server = docker.build("lsfusion/server:$tagVersion", "./server")
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