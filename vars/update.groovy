def call(String branch) {
    git(
            url: "https://github.com/" + Paths.githubRepo,
            branch: branch,
    )
}

def tag(String tag) {
    checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: 'https://github.com/' + Paths.githubRepo]], branches: [[name: tag]]]
}