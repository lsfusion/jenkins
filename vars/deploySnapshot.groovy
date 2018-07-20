def call(String branch) {
    stage ('update and deploy') {
        stages {
            update branch

            stage ('deploy') {
                steps {
                    sh "mvn -f ${Paths.src}/pom.xml clean deploy"
                }
            }
        }
    }
}
