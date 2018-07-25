def call() {
    def branches = ['v2', 'master']
    for (branch in branches) {
        deploySnapshot branch
    }
}

