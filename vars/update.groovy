def call(String branch) {
    node {
        dir(Paths.src) {
            git(
                    url: 'https://github.com/lsfusion/platform',
                    branch: branch
            )
        }
    }
}