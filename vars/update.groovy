def call(String branch) {
    stage('update') {
        dir(Paths.src) {
            dir('fsl') {
                git(
                        url: 'https://github.com/lsfusion/fsl',
                        branch: branch
                )
            }

            dir('platform') {
                git(
                        url: 'https://github.com/lsfusion/platform',
                        branch: branch
                )
            }
        }
    }
}