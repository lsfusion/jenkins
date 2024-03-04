def get() {
    withMaven {
        def betaVersion = sh script: 'mvn -N help:evaluate -Dexpression=next.beta.version -q -DforceStdout', returnStdout: true
        return betaVersion
    }
}

def set(nextBetaVersion) {
    withMaven {
        sh "mvn -N versions:set-property -Dproperty=next.beta.version -DnewVersion=$nextBetaVersion"
        sh "mvn scm:checkin -Dmessage=\"Changed next beta version\""
    }
}
