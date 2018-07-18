def call(String filePath, String folder) {
    withCredentials([usernameColonPassword(credentialsId: 'lsfusion.ftp', variable: 'USERPASS')]) {
        sh "curl -T ${filePath} --ftp-create-dirs ftp://ftp.keycdn.com/download/${folder}/ --user $USERPASS"
    }
}

