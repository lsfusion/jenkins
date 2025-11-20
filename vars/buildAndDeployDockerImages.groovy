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
            sh '''
              echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
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