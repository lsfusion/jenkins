def call(String message) {
    sh 'git commit -am ' + '"' + message + '"'
}

