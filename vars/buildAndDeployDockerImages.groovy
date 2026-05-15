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
            sh '''
              echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
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