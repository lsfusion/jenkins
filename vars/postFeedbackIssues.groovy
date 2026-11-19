// postFeedbackIssues — post the feedback reports the central MCP server
// stored on ai.lsfusion.org as issues in lsfusion/platform-feedback, one
// issue per report.
//
// Runs `python -m tools.feedback_issues` (mcp repo, shipped in the image)
// inside the running `mcp` container of the /opt/stack compose. The GitHub
// token never leaves the host: the remote shell feeds the root-only token file
// to the script's stdin, so it is on no command line and in no log. The script
// posts each report in <REPORTS_DIR>/inbox, moves it to processed/, and on a
// GitHub rate limit stops and leaves the rest for the next run. It exits 1 if
// a report failed or GitHub answered with an error, which fails the build.
//
// Runs must not overlap (two could post the same report twice), so the job
// needs DisableConcurrentBuilds. Reaches the host over passwordless SSH, as
// deployMcp does.
//
// Jenkins here is readable anonymously, so the dry run renders into a temp
// folder in the container and prints only file names and the summary — never
// the reports' text.
//
// Arguments (all optional):
//   dryRun:     render the issues, post nothing (default: false)
//   remoteHost: ssh target for the stack host   (default: 'root@ai.lsfusion.org')
//   stackDir:   compose project dir on the host (default: '/opt/stack')
//   service:    compose service running the mcp image (default: 'mcp')
//   tokenFile:  GitHub token on the host        (default: '/etc/lsfusion-feedback/github-token')

def call(Map args = [:]) {
    String dryRun     = args.dryRun ? 'true' : 'false'
    String remoteHost = args.remoteHost ?: 'root@ai.lsfusion.org'
    String stackDir   = args.stackDir   ?: '/opt/stack'
    String service    = args.service    ?: 'mcp'
    String tokenFile  = args.tokenFile  ?: '/etc/lsfusion-feedback/github-token'

    stage(args.dryRun ? 'Render feedback issues (dry run)' : 'Post feedback issues') {
        // Same shape as deployMcp: parameters go through the environment, the
        // remote script is a quoted heredoc expanded only by the remote bash.
        withEnv(["REMOTE_HOST=${remoteHost}", "STACK_DIR=${stackDir}", "SERVICE=${service}",
                 "TOKEN_FILE=${tokenFile}", "DRY_RUN=${dryRun}"]) {
            sh '''#!/usr/bin/env bash
set -euo pipefail
ssh -o BatchMode=yes -o StrictHostKeyChecking=accept-new "$REMOTE_HOST" \
    STACK_DIR="$STACK_DIR" SERVICE="$SERVICE" TOKEN_FILE="$TOKEN_FILE" DRY_RUN="$DRY_RUN" bash -s <<'REMOTE'
set -euo pipefail
cd "$STACK_DIR"
if docker compose version >/dev/null 2>&1; then DC="docker compose"; else DC="docker-compose"; fi
cid=$($DC ps -q "$SERVICE" 2>/dev/null || true)
[ -n "$cid" ] || { echo "[postFeedbackIssues] ERROR: $SERVICE has no running container"; exit 1; }
sha=$(docker inspect --format '{{range .Config.Env}}{{println .}}{{end}}' "$cid" | sed -n 's/^MCP_BUILD_SHA=//p' | cut -c1-7)
echo "[postFeedbackIssues] container ${cid:0:12}, mcp build ${sha:-unknown}"

if [ "$DRY_RUN" = true ]; then
    docker exec "$cid" sh -c 'out=$(mktemp -d) && python -m tools.feedback_issues --dry-run --out "$out"; rc=$?; rm -rf "$out"; exit $rc'
else
    [ -r "$TOKEN_FILE" ] || { echo "[postFeedbackIssues] ERROR: cannot read $TOKEN_FILE"; exit 1; }
    docker exec -i "$cid" python -m tools.feedback_issues < "$TOKEN_FILE"
fi
REMOTE
'''
        }
    }
}
