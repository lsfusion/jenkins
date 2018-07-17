def call(String jenkinsHome, String branch) {
    stage('update') {
        dir("${jenkinsHome}/src") {
            dir('fsl') {
                git(
                        url: 'https://github.com/lsfusion/fsl',
                        branch: "${branch}"
                )
            }

            dir('platform') {
                git(
                        url: 'https://github.com/lsfusion/platform',
                        branch: "${branch}"
                )
            }
        }
    }
}