def call(String branch) {
    stage('update') {
        dir("${Paths.src}/platfom") {
            git(
                    url: 'https://github.com/lsfusion/platform',
                    branch: branch
            )
        }
    }
}