def call(body) {
    def branches = ['release-candidate']
    for (branch in branches) {
        deploySnapshot branch
    }
}

