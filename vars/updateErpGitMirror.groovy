def call() {
    pipeline {
        agent any
        stages {
            stage('update') {
                steps {
                    dir("${Paths.jenkinsHome}/erp-git-mirror") {
                        sh "sudo git svn fetch"
                        sh "sudo git rebase remotes/trunk"
                        sh "sudo git push origin master"
                    }
                }
            }
        }
    }
}

