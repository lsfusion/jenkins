def call() {
    node {
        dir("${Paths.jenkinsHome}/erp-git-mirror") {
            sh "sudo git svn fetch"
            sh "sudo git rebase remotes/trunk"
            sh "sudo git push origin master"
        }
    }
}

