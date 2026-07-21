// Triggers the `build-all` GitHub Actions workflow of the flutter-client repository
// (https://github.com/lsfusion/flutter-client); see buildGithubWorkflow for options.

def call(Map args = [:]) {
    buildGithubWorkflow([repo: 'lsfusion/flutter-client'] + args)
}
