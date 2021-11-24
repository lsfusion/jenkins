def call(String message) {
    sh 'git diff-index --quiet HEAD || git commit -am ' + '"' + message + '"'
}

