def call() {
    def branches = ['v2']
    for (branch in branches) {
        deploySnapshot branch
    }
}

