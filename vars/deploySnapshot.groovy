def call(String branch) {
    node {
        update branch
        sh "mvn -f ${Paths.src}/pom.xml clean deploy"
    }
}
