def call(String branch) {
    update branch
    sh "mvn -f ${Paths.src}/pom.xml clean deploy"
}
