def call(String tagVersion) {

    registryCredential = 'docker-hub'
    client = ''
    server = ''

    stage('Building images') {
        script {
            client = docker.build("lsfusion/client:$tagVersion", "$Paths.src/web-client") // eg. lsfusion/client:4.0-beta4
            server = docker.build("lsfusion/server:$tagVersion", "$Paths.src/server")
        }
    }

    stage('Deploy images') {
        script {
            docker.withRegistry( '', registryCredential ) {
                client.push()
                server.push()
            }
        }
    }

    stage('Cleaning up') {
        sh "docker rmi lsfusion/client:$tagVersion"
        sh "docker rmi lsfusion/server:$tagVersion"
    }
}