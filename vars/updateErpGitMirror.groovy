def call() {
    node {
        dir("${Paths.jenkinsHome}/erp-git-mirror") {
            sh "git svn fetch"
            sh "git rebase remotes/trunk"
            sh "git push origin master"
        }
    }
}

