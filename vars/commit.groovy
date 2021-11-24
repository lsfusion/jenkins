def call(String message) {
    sh 'git diff-index HEAD || git commit -am ' + '"' + message + '"'
}

