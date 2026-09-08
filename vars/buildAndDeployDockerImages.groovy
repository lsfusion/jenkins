def call(String appVersion, String imageVersion) {

    stage('Setup buildx') {
        sh '''                    
            docker buildx rm multiarch-builder 2>/dev/null || true
            docker buildx create --name multiarch-builder --driver docker-container
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
            --provenance=false \
            --sbom=false \
            --platform linux/amd64,linux/arm64 \
            -t lsfusion/mycompany:$imageVersion \
            --build-arg FILENAME=lsfusion-server-${appVersion}.jar \
            --push \
            ./target
        """
    }

    stage('Cleaning up') {
        sh '''
            docker buildx prune -f
        '''
    }
}