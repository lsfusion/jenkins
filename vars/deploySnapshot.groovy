def call(String branch) {
    update branch
    sh "mvn clean deploy"
}
