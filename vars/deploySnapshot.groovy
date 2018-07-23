def call(String branch) {
    echo 'update'
    update branch
    echo 'deploy'
    println "mvn -f ${Paths.src}/pom.xml clean deploy".execute().text
}
