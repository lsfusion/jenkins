def call() {
    withCredentials([usernamePassword(credentialsId: '48021276-4f5f-4293-975e-ee13d9f97f51', passwordVariable: 'SVN_PASSWORD', usernameVariable: 'SVN_USERNAME')]) {
        sh "printf ${SVN_PASSWORD} | git svn fetch --username ${SVN_USERNAME}"
    }
    
    sh "git rebase remotes/trunk"
    sh "git push origin master"
}

