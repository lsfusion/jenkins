// Triggers the `build-all` GitHub Actions workflow of the flutter-client repository
// (https://github.com/lsfusion/flutter-client) via the workflow_dispatch API and,
// unless wait:false is passed, waits for the run to finish so that the result of the
// Flutter build is reflected in this Jenkins build's status.
//
// Usage from a pipeline job:
//     buildFlutterClient()                       // build-all on main, wait for result
//     buildFlutterClient(ref: 'feature/foo')     // build-all on another branch
//     buildFlutterClient(wait: false)            // fire-and-forget

def call(Map args = [:]) {
    String repo = args.repo ?: 'lsfusion/flutter-client'
    String workflow = args.workflow ?: 'build-all.yml'
    String ref = args.ref ?: 'main'
    boolean waitForResult = args.containsKey('wait') ? args.wait : true

    String workflowUrl = "https://github.com/${repo}/actions/workflows/${workflow}"

    try {
        withCredentials([string(credentialsId: 'api.github.com.token', variable: 'TOKEN')]) {
            // remember when we dispatched (with a small skew) to locate the started run afterwards
            String since = sh(script: "date -u -d '60 seconds ago' +%Y-%m-%dT%H:%M:%SZ", returnStdout: true).trim()

            echo "Dispatching ${workflow} on ${repo}@${ref}..."
            sh """
                curl -fsS -X POST \\
                  -H "Authorization: token \${TOKEN}" \\
                  -H "Accept: application/vnd.github+json" \\
                  -H "X-GitHub-Api-Version: 2022-11-28" \\
                  https://api.github.com/repos/${repo}/actions/workflows/${workflow}/dispatches \\
                  -d '{"ref": "${ref}"}'
            """

            if (!waitForResult) {
                echo "flutter-client `${workflow}` was triggered (ref: ${ref}). ${workflowUrl}"
                // slack.message "flutter-client `${workflow}` was triggered (ref: ${ref}).\n${workflowUrl}"
                return
            }

            def run = waitForRun(repo, workflow, ref, since)
            echo "Run started: ${run.html_url}"

            String conclusion = waitForCompletion(repo, run.id)
            if (conclusion == 'success') {
                echo "flutter-client `${workflow}` finished successfully (ref: ${ref}). ${run.html_url}"
                // slack.message "flutter-client `${workflow}` finished successfully (ref: ${ref}).\n${run.html_url}"
            } else {
                // slack.warning "flutter-client `${workflow}` finished with '${conclusion}' (ref: ${ref}).\n${run.html_url}"
                error "flutter-client ${workflow} run concluded with '${conclusion}': ${run.html_url}"
            }
        }
    } catch (e) {
        // slack.error "Warning! <$env.BUILD_URL|$currentBuild.fullDisplayName> failed."
        throw e
    }
}

// Polls until the workflow_dispatch run we just triggered shows up, then returns it.
def waitForRun(String repo, String workflow, String ref, String since) {
    String url = "https://api.github.com/repos/${repo}/actions/workflows/${workflow}/runs" +
            "?branch=${ref}&event=workflow_dispatch&per_page=10"
    for (int i = 0; i < 24; i++) {
        def runs = githubJson(url).workflow_runs
        for (int j = 0; j < runs.size(); j++) {
            def run = runs[j]
            if (run.created_at >= since) {
                return run
            }
        }
        sleep 5
    }
    error "Could not find the dispatched ${workflow} run on ${repo}@${ref} after dispatch."
}

// Polls a run until it is completed, returns its conclusion (success/failure/cancelled/...).
def waitForCompletion(String repo, runId) {
    String url = "https://api.github.com/repos/${repo}/actions/runs/${runId}"
    for (int i = 0; i < 240; i++) { // up to ~2h at 30s steps
        def run = githubJson(url)
        if (run.status == 'completed') {
            return run.conclusion
        }
        sleep 30
    }
    error "Timed out waiting for ${repo} run ${runId} to complete."
}

// GET an authenticated GitHub API endpoint and parse the JSON response.
// Must be called inside the withCredentials block that exposes TOKEN.
def githubJson(String url) {
    String response = sh(
            script: """curl -fsS -H "Authorization: token \${TOKEN}" -H "Accept: application/vnd.github+json" "${url}" """,
            returnStdout: true
    )
    return readJSON(text: response)
}
