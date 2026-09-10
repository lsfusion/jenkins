// ragBuildSnapshot — post-merge step for lsfusion/platform master.
//
// Runs after a commit lands on master. Sparse-clones the mcp repo at the
// SHA pinned in platform/.rag/mcp-version, installs the runtime deps in a
// fresh venv, builds the corpus snapshot the MCP server searches
// in-process, and copies it to the server host over SSH.
//
// It used to do two things: index the docs into an OpenAI vector store AND
// build this snapshot, from the same Section objects, so the two could never
// describe different revisions. Nothing reads the vector store any more — the
// server searches the snapshot, and the snapshot was never built from the
// store — so the ingest half, its state ledger and its credential are gone,
// and this is what is left.
//
// Required Jenkins credentials (configure on the job):
//   - openai-api-key (Secret text): OpenAI API key. Still needed — the
//     snapshot's vectors are embeddings, and this is where they are made.
//
// CONCURRENCY: the wrapper holds `lock(resource: 'platform-rag-state')` for
// its whole body. It no longer shares that lock with anything (ragRebuildIndex
// went with the store), but two overlapping docs commits would otherwise race
// on the same workspace and the same snapshot file on the server. Requires the
// Lockable Resources plugin; without it the build fails loudly, which is the
// intended behaviour over silent racing.
//
// Call example — the step checks out platform master itself, so no separate
// Checkout stage is needed:
//
//   @Library('lsfusion') _
//   pipeline {
//     agent any
//     stages {
//       stage('Snapshot') { steps { ragBuildSnapshot() } }
//     }
//   }

def call(Map args = [:]) {
    String platformRoot = args.platformRoot  ?: '.'
    Boolean dryRun      = args.dryRun        ?: false
    // The snapshot the MCP server searches in-process. Built from the SAME
    // Section objects this job just indexed, in this job, so the snapshot and
    // the vector store can never describe different revisions of the docs.
    // Delivered to the host that runs the server — the one deployMcp already
    // reaches over SSH.
    Boolean buildSnapshot = args.containsKey('buildSnapshot') ? args.buildSnapshot : true
    String snapshotHost   = args.snapshotHost ?: 'root@ai.lsfusion.org'
    String snapshotDir    = args.snapshotDir  ?: '/opt/stack/mcp-data/snapshot'
    String snapshotName   = args.snapshotName ?: 'corpus.npz'

    // Every operator-supplied string that lands inside a `sh` heredoc is
    // single-quote-escaped via the standard bash trick: `'` → `'\''`.
    // The shell sees the closing quote, an escaped literal `'`, then a
    // re-opened quote — round-trips any value without injection.
    String platformRootSh = platformRoot.replace("'", "'\\''")

    // Check out platform master into platformRoot first: the mcp pin (.rag/mcp-version),
    // and the docs corpus the snapshot is built from, both live on master.
    // Doing it here makes the step self-contained, so callers (the processPlatformCommit
    // merge job, or a standalone job) don't have to check out master beforehand.
    dir(platformRoot) {
        update "master"
    }

    String mcpVersionFile = "${platformRoot}/.rag/mcp-version"
    if (!fileExists(mcpVersionFile)) {
        error("ragBuildSnapshot: ${mcpVersionFile} not found. This step expects to run on " +
              "a platform-repo checkout that has been bootstrapped (mcp pin committed).")
    }
    String mcpSha = readFile(mcpVersionFile).trim()
    if (!(mcpSha ==~ /[0-9a-f]{7,40}/)) {
        error("ragBuildSnapshot: .rag/mcp-version does not look like a git SHA: '${mcpSha}'")
    }

    // mcpDir and venvDir live at workspace root regardless of platformRoot —
    // they're build-time scratch, not part of the platform tree.
    String mcpDir = '.jenkins-mcp'
    String venvDir = '.jenkins-rag-venv'

    // Everything from clone → run → commit → push holds the shared lock,
    // so two overlapping docs commits cannot race on the snapshot.
    // Argument parsing and the pin-file sanity checks above run unlocked
    // (they don't touch the state file). The lock is shared with
    // ragRebuildIndex (same resource name). Requires the Lockable
    // Resources plugin; without it, Jenkins fails the build with a clear
    // error, which is the desired behavior — silent racing is worse.
    lock(resource: 'platform-rag-state') {

    // ─── 1. Sparse-clone mcp at the pinned SHA ─────────────────────────────
    // Pulls just `fill/` and `tools/` (the driver lives in tools/). Public
    // repo, but cloned over SSH: GitHub throttles unauthenticated downloads
    // from datacenter IPs, which fails this clone intermittently.
    dir(mcpDir) { deleteDir() }
    sh """#!/usr/bin/env bash
set -euo pipefail
git clone --filter=blob:none --no-checkout 'git@github.com:${mcpRepo}' '${mcpDir}'
cd '${mcpDir}'
git sparse-checkout init --cone
git sparse-checkout set fill tools
git checkout '${mcpSha}'
"""

    // ─── 2. Install Python deps in a fresh venv ───────────────────────────
    // Fresh-per-run ensures a stale pin doesn't leave dangling installs
    // from a previous mcp version with different deps. The dry-run path
    // skips the openai SDK install (FakeVectorStoreClient is stdlib-only).
    //
    // Versions are intentionally unpinned: the OpenAI SDK ships breaking
    // changes occasionally and tracking a fixed version would freeze the
    // pipeline. If a build breaks from an SDK update, pin here on the spot.
    // Internal constants only — no operator input. Each package name is
    // single-quoted to keep the shell expansion explicit even though
    // injection is currently impossible.
    // `numpy` is the snapshot builder's only extra dep (a snapshot is a
    // float32 matrix); installed on the real path only, since a dry run
    // never builds one.
    // One list now. The split existed because the dry-run path drove a fake
    // vector-store client that was stdlib-only; building the snapshot needs
    // `openai` to embed and `numpy` to write, whatever the mode.
    String pipPackages = "'openai' 'langchain-text-splitters' 'tiktoken' 'python-frontmatter' 'numpy'"
    sh """#!/usr/bin/env bash
set -euo pipefail
rm -rf '${venvDir}'
python3 -m venv '${venvDir}'
. '${venvDir}'/bin/activate
pip install --quiet --upgrade pip
pip install --quiet ${pipPackages}
"""

    // ─── 3. Build and deliver the corpus snapshot ─────────────────────────
    // Delivery is atomic on the far side — copy to a temp name, then `mv`
    // within the same directory — because the server may be reading the file
    // while this runs, and half a snapshot loads as a truncated one. Mode 0644
    // on purpose: the container runs as uid 10001, not root.
    //
    // A failure here does NOT fail the build: the server keeps serving the
    // snapshot it already has, and the next docs commit retries. That is now
    // the ONLY safety net — there is no second index to fall back to — so a
    // run that fails here leaves the server answering from documentation that
    // has moved on, and the snapshot's own age warning is what says so.
    if (dryRun) {
        echo 'ragBuildSnapshot: dry-run — skipping the snapshot build'
    } else if (!buildSnapshot) {
        echo 'ragBuildSnapshot: buildSnapshot=false — skipping the snapshot build'
    } else {
        String docsSha = ''
        dir(platformRoot) {
            docsSha = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
        }
        withCredentials([string(credentialsId: 'openai-api-key', variable: 'OPENAI_API_KEY')]) {
            withEnv(["SNAP_HOST=${snapshotHost}", "SNAP_DIR=${snapshotDir}",
                     "SNAP_NAME=${snapshotName}", "DOCS_SHA=${docsSha}",
                     "MCP_DIR=${mcpDir}", "VENV_DIR=${venvDir}",
                     "PLATFORM_ROOT=${platformRoot}"]) {
                // Single-quoted on purpose: no Groovy interpolation, so no
                // $-escaping landmines. Every value arrives through the env
                // set just above.
                int snapRc = sh(returnStatus: true, script: '''#!/usr/bin/env bash
set -euo pipefail
. "$VENV_DIR"/bin/activate
OUT="$WORKSPACE/rag-snapshot.npz"
PYTHONPATH="$MCP_DIR" python3 "$MCP_DIR/tools/rag_build_snapshot.py" \\
    --platform-root "$PLATFORM_ROOT" --out "$OUT" --corpus-revision "$DOCS_SHA"
ls -l "$OUT"
scp -o BatchMode=yes -o StrictHostKeyChecking=accept-new \\
    "$OUT" "$SNAP_HOST:$SNAP_DIR/$SNAP_NAME.tmp"
ssh -o BatchMode=yes -o StrictHostKeyChecking=accept-new "$SNAP_HOST" \\
    "chmod 0644 '$SNAP_DIR/$SNAP_NAME.tmp' && mv -f '$SNAP_DIR/$SNAP_NAME.tmp' '$SNAP_DIR/$SNAP_NAME'"
rm -f "$OUT"
''')

                if (snapRc != 0) {
                    echo "ragBuildSnapshot: snapshot build/delivery failed (rc=${snapRc}) — the " +
                         'server keeps the snapshot it already has, and the next docs ' +
                         'commit retries. Nothing else serves this documentation.'
                } else {
                    echo "ragBuildSnapshot: snapshot delivered to ${snapshotHost}:${snapshotDir}/${snapshotName} " +
                         "for docs ${docsSha}"
                }
            }
        }
    }

    }  // end lock
}