def get() {
    def betaVersion = sh script: 'mvn -N help:evaluate -Dexpression=latest.beta.version -q -DforceStdout', returnStdout: true
    return betaVersion
}

def set(newBetaVersion) {
    sh "mvn -N versions:set-property -Dproperty=latest.beta.version -DnewVersion=$newBetaVersion"
    sh "mvn scm:checkin -Dmessage=\"Changed latest beta version\""
}
