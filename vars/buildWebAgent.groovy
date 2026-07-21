// Triggers the `build-all` GitHub Actions workflow of the web-agent repository
// (https://github.com/lsfusion/web-agent); see buildGithubWorkflow for options.

def call(Map args = [:]) {
    buildGithubWorkflow([repo: 'lsfusion/web-agent'] + args)
}
