def call(String branch) {
    withCredentials([usernamePassword(credentialsId: Paths.githubCredentials, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        sh 'git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/' + Paths.githubRepo + '.git ' + branch + ":" + branch
    }
}

