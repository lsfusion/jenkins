def call(String branch) {
    stage('update') {
        dir("${Paths.src}") {
            git(
                    url: 'https://github.com/lsfusion/platform',
                    branch: branch
            )
        }
    }
}