def call(String branch) {
    node {
        update branch
        dir(Paths.src) {
            sh "mvn clean deploy"
        }
    }
}
