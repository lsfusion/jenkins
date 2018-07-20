def call(String branch) {

    pipeline {
        agent any
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
