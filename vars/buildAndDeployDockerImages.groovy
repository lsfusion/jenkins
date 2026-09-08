def call(String tagVersion) {

    stage('Setup buildx') {
        sh '''
            docker run --rm --privileged multiarch/qemu-user-static --reset -p yes

            # Creating builder, if it is absent
            if ! docker buildx ls | grep -q multiarch-builder; then
              docker buildx create --name multiarch-builder --driver docker-container
            fi

            docker buildx inspect multiarch-builder --bootstrap
        '''
    }

    // withRegistry creates new DOCKER_CONFIG - multiarch-builder is not visible
    stage('Docker login') {
        withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
            // dockerd gives the whole login (registry ping + token request) a hardcoded 15s
            // budget, so a short network hiccup is enough to fail it - retry with a backoff
            sh '''
              for attempt in 1 2 3 4 5; do
                if echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin; then
                  exit 0
                fi
                echo "docker login failed (attempt $attempt of 5), retrying in $((attempt * 10))s"
                sleep $((attempt * 10))
              done
              echo "docker login failed after 5 attempts"
              exit 1
            '''
        }
    }

    stage('Building & deploying images') {
        sh """
          docker buildx build \
            --builder multiarch-builder \
            --platform linux/amd64,linux/arm64 \
            -t lsfusion/client:${tagVersion} \
            --push \
            ${Paths.src}/web-client
        """

        sh """
          docker buildx build \
            --builder multiarch-builder \
            --platform linux/amd64,linux/arm64 \
            -t lsfusion/server:${tagVersion} \
            --push \
            ${Paths.src}/server
        """
    }

    stage('Cleaning up') {
        sh '''
            docker buildx prune -f
        '''
    }
}