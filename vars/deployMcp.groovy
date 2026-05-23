// deployMcp — pull ghcr.io/lsfusion/mcp:latest and recreate the `mcp`
// service of the /opt/stack compose on the ai.lsfusion.org host.
//
// Triggered by the mcp repo's CI (.github/workflows/docker.yml) right after
// it pushes the :latest image to GHCR, so the new image is guaranteed to be
// present before this job pulls. Also safe to run manually.
//
// The Jenkins agent reaches the host over passwordless SSH — same convention
// as buildRPMInstallers' `remoteRedHat`. Only the `mcp` service is recreated;
// proxy / proxy-mcp / caddy keep running and reconnect to it via docker DNS.
//
// NOTE: recreating the container drops in-flight MCP sessions — connected
// clients get HTTP 404 "Session not found" until they re-initialize (see the
// MCPRemoteClient resilience note). That is inherent to any redeploy.
//
// Arguments (all optional):
//   remoteHost: ssh target for the stack host   (default: 'root@ai.lsfusion.org')
//   stackDir:   compose project dir on the host (default: '/opt/stack')
//   service:    compose service to redeploy     (default: 'mcp')

def call(Map args = [:]) {
    String remoteHost = args.remoteHost ?: 'root@ai.lsfusion.org'
    String stackDir   = args.stackDir   ?: '/opt/stack'
    String service    = args.service    ?: 'mcp'

    stage("Redeploy ${service}") {
        // Pass parameters through the environment so the shell body can stay
        // single-quoted (no Groovy interpolation → no $-escaping landmines).
        // The remote script is a single quoted heredoc, expanded only by the
        // remote bash against the env we forward on the ssh command line.
        withEnv(["REMOTE_HOST=${remoteHost}", "STACK_DIR=${stackDir}", "SERVICE=${service}"]) {
            sh '''#!/usr/bin/env bash
set -euo pipefail
ssh -o BatchMode=yes -o StrictHostKeyChecking=accept-new "$REMOTE_HOST" \
    STACK_DIR="$STACK_DIR" SERVICE="$SERVICE" bash -s <<'REMOTE'
set -euo pipefail
cd "$STACK_DIR"
if docker compose version >/dev/null 2>&1; then DC="docker compose"; else DC="docker-compose"; fi
echo "[deployMcp] compose=$DC dir=$STACK_DIR service=$SERVICE"

cid=$($DC ps -q "$SERVICE" 2>/dev/null || true)
before=$([ -n "$cid" ] && docker inspect --format '{{.Image}}' "$cid" || echo none)
echo "[deployMcp] image before: $before"

$DC pull "$SERVICE"
$DC up -d "$SERVICE"

cid=$($DC ps -q "$SERVICE" 2>/dev/null || true)
after=$([ -n "$cid" ] && docker inspect --format '{{.Image}}' "$cid" || echo none)
echo "[deployMcp] image after:  $after"
[ -n "$cid" ] || { echo "[deployMcp] ERROR: $SERVICE has no running container after up -d"; exit 1; }

docker image prune -f >/dev/null 2>&1 || true
echo "[deployMcp] done"
REMOTE
'''
        }
    }
}
