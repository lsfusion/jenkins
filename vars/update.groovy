def call(String branch) {
    dir(Paths.src) {
        git(
                url: 'https://github.com/lsfusion/platform',
                branch: branch
        )
    }
}