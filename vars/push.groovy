def call(String branch) {
    sh 'git push git@github.com:' + Paths.githubRepo + ' ' + branch + ":" + branch
}

