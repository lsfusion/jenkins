def call() {
    Integer lastVersion
    (lastVersion, lastVersionState, lastSupportedVersion) = getLastVersions()
    pipeline {
        environment {
            registryCredential = 'docker-hub'
            client = ''
            server = ''
        }

        agent any

        stages {
            stage('Git update') {
                steps {
                    update "v$lastVersion"
                }
            }

            stage('Building images') {
                steps {
                    script {
                        client = docker.build("lsfusion/client:$lastVersion-build_$BUILD_NUMBER", "./web-client")
                        server = docker.build("lsfusion/server:$lastVersion-build_$BUILD_NUMBER", "./server")
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
                    sh "docker rmi lsfusion/client:$lastVersion-build_$BUILD_NUMBER"
                    sh "docker rmi lsfusion/server:$lastVersion-build_$BUILD_NUMBER"
                }
            }
        }
    }
}