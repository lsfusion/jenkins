// ragIngestDocs — post-merge step for lsfusion/platform master.
//
// Runs after a commit lands on master. Sparse-clones the mcp repo at the
// SHA pinned in platform/.rag/mcp-version, installs the runtime deps in
// a fresh venv (openai SDK, langchain-text-splitters, tiktoken,
// python-frontmatter), runs `tools/rag_ingest_docs.py` against the
// platform workspace, and commits + pushes the updated state file back
// to platform/master.
//
// The driver itself does the incremental git-diff and decides which docs
// to (re)index; this wrapper just plumbs credentials, deps, and the
// state-commit back to the remote.
//
// It ALSO builds the corpus snapshot the MCP server searches in-process and
// copies it to the server host. That happens here, from the same Section
// objects the driver just indexed, so the snapshot and the vector store can
// never describe different revisions of the docs — which is the one failure
// a local index has that a hosted one does not.
//
// Required Jenkins credentials (configure on the job):
//   - openai-api-key       (Secret text): OpenAI API key.
//   - rag-vector-store-id  (Secret text): target Vector Store id (vs_...).
//     Only consulted on the first run; persisted into state.json thereafter.
//
// CONCURRENCY: the wrapper internally holds `lock(resource:
// 'platform-rag-state')` for its whole body. ragRebuildIndex uses the
// same lock name, so the two jobs serialize on each other (which is what
// we want — both write `.rag/openai-state.json`). This requires the
// Lockable Resources Jenkins plugin; without it the build fails loudly,
// which is the intended behavior over silent racing.
//
// Call example — the step checks out platform master itself (see below), so no separate
// Checkout stage is needed; just invoke it (e.g. from the processPlatformCommit merge job,
// or a standalone job for manual / dry runs):
//
//   @Library('lsfusion') _
//   pipeline {
//     agent any
//     options { disableConcurrentBuilds() }
//     stages {
//       stage('Ingest') { steps { ragIngestDocs() } }
//     }
//   }
//
// Arguments (all optional):
//   platformRoot:  workspace-relative path to the platform clone (default: '.')
//   pushBranch:    branch to push the state commit to            (default: 'master')
//   gitUserName:   commit author name                            (default: 'rag-ingest[bot]')
//   gitUserEmail:  commit author email                           (default: 'rag-ingest@lsfusion.org')
//   dryRun:        skip real OpenAI calls; uses FakeVectorStoreClient.
//                  ALSO skips commit + push (must not push fake state).
//                  Useful for smoke-testing the pipeline.        (default: false)

import static Paths.mcpRepo
import static Paths.githubRepo

def call(Map args = [:]) {
    String platformRoot = args.platformRoot  ?: '.'
    String pushBranch   = args.pushBranch    ?: 'master'
    String gitUserName  = args.gitUserName   ?: 'rag-ingest[bot]'
    String gitUserEmail = args.gitUserEmail  ?: 'rag-ingest@lsfusion.org'
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
    String gitUserNameSh  = gitUserName.replace("'", "'\\''")
    String gitUserEmailSh = gitUserEmail.replace("'", "'\\''")
    // pushBranch is additionally regex-validated as a shell-safe subset
    // before any shell uses it. The in-shell `git check-ref-format` later
    // catches things like `..` and `.lock` that this regex permits.
    if (!(pushBranch ==~ /[a-zA-Z0-9._\/-]+/)) {
        error("ragIngestDocs: pushBranch contains unsafe characters: '${pushBranch}'")
    }

    // Check out platform master into platformRoot first: the mcp pin (.rag/mcp-version),
    // the docs corpus, and the .rag/openai-state.json this step commits all live on master.
    // Doing it here makes the step self-contained, so callers (the processPlatformCommit
    // merge job, or a standalone job) don't have to check out master beforehand.
    dir(platformRoot) {
        update "master"
    }

    String mcpVersionFile = "${platformRoot}/.rag/mcp-version"
    if (!fileExists(mcpVersionFile)) {
        error("ragIngestDocs: ${mcpVersionFile} not found. This step expects to run on " +
              "a platform-repo checkout that has been bootstrapped (mcp pin committed).")
    }
    String mcpSha = readFile(mcpVersionFile).trim()
    if (!(mcpSha ==~ /[0-9a-f]{7,40}/)) {
        error("ragIngestDocs: .rag/mcp-version does not look like a git SHA: '${mcpSha}'")
    }

    // mcpDir and venvDir live at workspace root regardless of platformRoot —
    // they're build-time scratch, not part of the platform tree.
    String mcpDir = '.jenkins-mcp'
    String venvDir = '.jenkins-rag-venv'

    // Everything from clone → run → commit → push holds the shared lock,
    // so ingest-vs-rebuild can never race on .rag/openai-state.json.
    // Argument parsing and the pin-file sanity checks above run unlocked
    // (they don't touch the state file). The lock is shared with
    // ragRebuildIndex (same resource name). Requires the Lockable
    // Resources plugin; without it, Jenkins fails the build with a clear
    // error, which is the desired behavior — silent racing is worse.
    lock(resource: 'platform-rag-state') {

    // ─── 1. Sparse-clone mcp at the pinned SHA ─────────────────────────────
    // Pulls just `fill/` and `tools/` (the driver lives in tools/). Public
    // repo, no creds needed.
    dir(mcpDir) { deleteDir() }
    sh """#!/usr/bin/env bash
set -euo pipefail
git clone --filter=blob:none --no-checkout 'https://github.com/${mcpRepo}' '${mcpDir}'
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
    String pipPackages = dryRun
        ? "'langchain-text-splitters' 'tiktoken' 'python-frontmatter'"
        : "'openai' 'langchain-text-splitters' 'tiktoken' 'python-frontmatter' 'numpy'"
    sh """#!/usr/bin/env bash
set -euo pipefail
rm -rf '${venvDir}'
python3 -m venv '${venvDir}'
. '${venvDir}'/bin/activate
pip install --quiet --upgrade pip
pip install --quiet ${pipPackages}
"""

    // ─── 3. Run the driver ────────────────────────────────────────────────
    // Setup commands run under `set -e` (a venv activation failure must
    // fail the build, not silently fall through to system python). Only
    // the python invocation itself is wrapped with `set +e ... set -e` so
    // its exit code is captured for step 4. Driver exit 1 = "ran but had
    // errors, state saved" — we still want to commit that state because
    // the driver is idempotent (fast-path skips done files); leaving the
    // partial result out of git would make the next run redo everything.
    if (dryRun) {
        sh """#!/usr/bin/env bash
set -euo pipefail
. '${venvDir}'/bin/activate
set +e
PYTHONPATH='${mcpDir}' python3 '${mcpDir}/tools/rag_ingest_docs.py' \\
    --platform-root '${platformRootSh}' --dry-run
RC=\$?
set -e
echo \$RC > .jenkins-rag-rc
"""
    } else {
        withCredentials([
            string(credentialsId: 'openai-api-key',      variable: 'OPENAI_API_KEY'),
            string(credentialsId: 'rag-vector-store-id', variable: 'RAG_VECTOR_STORE_ID'),
        ]) {
            sh """#!/usr/bin/env bash
set -euo pipefail
. '${venvDir}'/bin/activate
set +e
PYTHONPATH='${mcpDir}' python3 '${mcpDir}/tools/rag_ingest_docs.py' \\
    --platform-root '${platformRootSh}'
RC=\$?
set -e
echo \$RC > .jenkins-rag-rc
"""
        }
    }

    String driverRc = readFile('.jenkins-rag-rc').trim()

    // ─── 3b. Build and deliver the corpus snapshot ────────────────────────
    // Only on a clean driver run: a snapshot built beside a half-finished
    // ingest would describe a corpus the store does not hold, and the whole
    // point of building it here is that the two agree.
    //
    // Delivery is atomic on the far side — copy to a temp name, then `mv`
    // within the same directory — because the server may be reading the file
    // while this runs, and half a snapshot loads as a truncated one. Mode 0644
    // on purpose: the container runs as uid 10001, not root.
    //
    // A failure here does NOT fail the build. The ingest already succeeded and
    // the vector store is up to date; the server keeps serving its previous
    // snapshot (or falls back to the store), and the next docs commit retries.
    if (dryRun) {
        echo 'ragIngestDocs: dry-run — skipping the snapshot build'
    } else if (!buildSnapshot) {
        echo 'ragIngestDocs: buildSnapshot=false — skipping the snapshot build'
    } else if (driverRc != '0') {
        echo "ragIngestDocs: driver exit ${driverRc} — skipping the snapshot " +
             '(it must describe the same corpus the store just received)'
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
                    echo "ragIngestDocs: snapshot build/delivery failed (rc=${snapRc}) — the " +
                         'ingest stands, the server keeps its previous snapshot, and the ' +
                         'next docs commit retries'
                } else {
                    echo "ragIngestDocs: snapshot delivered to ${snapshotHost}:${snapshotDir}/${snapshotName} " +
                         "for docs ${docsSha}"
                }
            }
        }
    }

    // ─── 4. Commit + push state.json (skipped on dry-run) ────────────────
    // Dry-run uses a Fake client; pushing its mutations back to master
    // would corrupt real state. The guard is hard.
    //
    // We commit on exit 0 (clean) and exit 1 (per-file errors, state saved
    // and self-consistent). Any other code (2 = pre-save setup error;
    // anything else = unhandled crash, state integrity unknown) skips the
    // commit — there's no benefit to publishing potentially-corrupt state.
    boolean driverProducedCommittableState = driverRc == '0' || driverRc == '1'
    if (dryRun) {
        echo "ragIngestDocs: dry-run — skipping commit + push (driver exit ${driverRc})"
    } else if (!driverProducedCommittableState) {
        echo "ragIngestDocs: driver exit ${driverRc} — skipping commit (state integrity unknown)"
    } else {
        dir(platformRoot) {
            // `git add` BEFORE the diff check is critical: an untracked
            // state.json (first run) isn't seen by plain `git diff`, only
            // by the staged diff once it's added.
            //
            // The fetch runs FIRST (before commit) so that a network /
            // permissions failure aborts the build before we've created a
            // local commit that would otherwise sit dangling.
            sh """#!/usr/bin/env bash
set -euo pipefail

# Stricter than the wrapper's shell-safe regex: git's own refname rules.
# Catches names like `x..y`, `x.lock`, `-x` that pass the shell check.
git check-ref-format --branch '${pushBranch}' >/dev/null

git config --local user.name  '${gitUserNameSh}'
git config --local user.email '${gitUserEmailSh}'

# Explicit refspec: `<ref>:<dst>` forces the remote-tracking branch
# update so the rebase target below is fresh. Plain `git fetch origin
# <ref>` only updates FETCH_HEAD on some configurations, leaving
# `origin/<branch>` stale.
git fetch origin '${pushBranch}:refs/remotes/origin/${pushBranch}'

git add .rag/openai-state.json
if git diff --cached --quiet -- .rag/openai-state.json; then
    echo '.rag/openai-state.json unchanged — no commit needed'
    exit 0
fi
git commit -m 'rag: state update after ingest cycle'
git rebase 'origin/${pushBranch}' || {
    echo 'rag: rebase onto origin/${pushBranch} failed (likely a true conflict in .rag/)'
    git rebase --abort || true
    exit 1
}
git push 'git@github.com:${githubRepo}' "HEAD:${pushBranch}"
"""
        }
    }

    // Propagate the driver's exit code so Jenkins marks the build accordingly.
    if (driverRc != '0') {
        error("ragIngestDocs: driver exited ${driverRc} (state was committed if dry-run=false)")
    }

    }  // end lock
}
