def call(String tagVersion) {

    stage('Setup buildx') {
        steps {
            sh '''
              docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
              docker buildx create --use --name multiarch-builder || docker buildx use multiarch-builder
              docker buildx inspect --bootstrap
            '''
        }
    }

    stage('Building & deploying images') {
        script {
            docker.withRegistry('', 'docker-hub') {
                sh """
                  docker buildx build \
                    --platform linux/amd64,linux/arm64 \
                    -t lsfusion/client:$tagVersion \
                    --push \
                    $Paths.src/web-client
                """

                sh """
                  docker buildx build \
                    --platform linux/amd64,linux/arm64 \
                    -t lsfusion/server:$tagVersion \
                    --push \
                    $Paths.src/server
                """
            }
        }
    }

    stage('Cleaning up') {
        sh 'docker buildx prune -f'
    }
}