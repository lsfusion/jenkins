// The platform's browser tests (its GitHub workflow "Browser tests") of a branch, run on GitHub rather than here: call
// starts a run and leaves, check tells Slack later if that run failed.

// with the tests of the commit the branch's snapshots were last deployed from, which deploySnapshot writes down, as the
// workflow takes the war and the server from those snapshots; before the first such deploy, of the branch's last commit
def call(String branch) {
    File deployed = new File("${Paths.jenkinsHome}/deployedCommit-${branch}")
    buildGithubWorkflow(repo: 'lsfusion/platform', workflow: 'ui-tests.yml', ref: branch, wait: false,
            inputs: [commit: deployed.exists() ? deployed.text : ''])
}

// the latest run within a day that the token's user started, which a run by hand is not
def check(String branch) {
    withCredentials([string(credentialsId: 'api.github.com.token', variable: 'TOKEN')]) {
        String since = sh(returnStdout: true, script: "date -u -d '1 day ago' +%Y-%m-%dT%H:%M:%SZ").trim()
        String actor = buildGithubWorkflow.githubJson('https://api.github.com/user').login
        def run = buildGithubWorkflow.githubJson('https://api.github.com/repos/lsfusion/platform/actions/workflows/ui-tests.yml/runs' +
                "?branch=${branch}&event=workflow_dispatch&actor=${actor}&per_page=1").workflow_runs[0]
        if (run != null && run.created_at >= since && run.conclusion == 'failure')
            slack.error "Browser tests of ${branch} failed: ${run.html_url}"
    }
}
