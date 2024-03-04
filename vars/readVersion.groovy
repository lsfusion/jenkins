def call() {
    withMaven {
        sh 'mvn -N help:effective-pom -Doutput=effective-pom.xml'
    }
    pom = readMavenPom file: 'effective-pom.xml'
    return pom.version
}

