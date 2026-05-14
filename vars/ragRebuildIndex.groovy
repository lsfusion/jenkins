// ragRebuildIndex — operator-triggered RAG state recovery on lsfusion/platform.
//
// Rebuilds platform/.rag/openai-state.json by listing the OpenAI Vector
// Store and treating it as source of truth. Sparse-clones mcp at the
// pinned SHA, installs runtime deps in a fresh venv, runs
// `tools/rag_rebuild_index.py`, then commits + pushes the rebuilt state
// back to platform/master.
//
// When to run (manually trigger this job):
//   - state.json was accidentally deleted or corrupted.
//   - You suspect an ingest run committed inconsistent state.
//   - You're investigating drift between state and the actual VS.
//
// NOT for regular indexing — that's `ragIngestDocs` (post-merge).
// NOT for switching to a new VS — use `ragIngestDocs(--vector-store-id ...)`
// which wipes state AND uploads docs to the new VS. This driver instead
// expects the target VS to already contain indexed files.
//
// Required Jenkins credentials (configure on the job):
//   - openai-api-key       (Secret text): OpenAI API key.
//   - rag-vector-store-id  (Secret text): target Vector Store id (vs_...).
//     Used as a FALLBACK only — once state.json has a persisted vs_id,
//     it takes priority over this credential. To switch state.json to a
//     different VS, the operator runs the driver locally with
//     --vector-store-id (or rotates the credential after clearing
//     state.vector_store_id).
//
// CONCURRENCY: the wrapper internally holds `lock(resource:
// 'platform-rag-state')` for its whole body. ragIngestDocs uses the same
// lock name, so the two jobs serialize on each other (which is what we
// want — both write `.rag/openai-state.json`). This requires the
// Lockable Resources Jenkins plugin; without it the build fails loudly,
// which is the intended behavior over silent racing.
//
// Call example (Jenkinsfile on a manual-trigger job):
//
//   @Library('lsfusion') _
//   pipeline {
//     agent any
//     options { disableConcurrentBuilds() }
//     stages {
//       stage('Checkout') { steps { checkout scm } }
//       stage('Rebuild')  { steps { ragRebuildIndex() } }
//     }
//   }
//
// Arguments (all optional):
//   platformRoot:  workspace-relative path to the platform clone (default: '.')
//   pushBranch:    branch to push the state commit to            (default: 'master')
//   gitUserName:   commit author name                            (default: 'rag-ingest[bot]')
//   gitUserEmail:  commit author email                           (default: 'rag-ingest@lsfusion.org')
//   dryRun:        skip real OpenAI calls; uses FakeVectorStoreClient.
//                  ALSO skips commit + push.                     (default: false)

import static Paths.mcpRepo
import static Paths.githubRepo

def call(Map args = [:]) {
    String platformRoot = args.platformRoot  ?: '.'
    String pushBranch   = args.pushBranch    ?: 'master'
    String gitUserName  = args.gitUserName   ?: 'rag-ingest[bot]'
    String gitUserEmail = args.gitUserEmail  ?: 'rag-ingest@lsfusion.org'
    Boolean dryRun      = args.dryRun        ?: false

    // Every operator-supplied string that lands inside a `sh` heredoc is
    // single-quote-escaped via the standard bash trick.
    String platformRootSh = platformRoot.replace("'", "'\\''")
    String gitUserNameSh  = gitUserName.replace("'", "'\\''")
    String gitUserEmailSh = gitUserEmail.replace("'", "'\\''")
    if (!(pushBranch ==~ /[a-zA-Z0-9._\/-]+/)) {
        error("ragRebuildIndex: pushBranch contains unsafe characters: '${pushBranch}'")
    }

    String mcpVersionFile = "${platformRoot}/.rag/mcp-version"
    if (!fileExists(mcpVersionFile)) {
        error("ragRebuildIndex: ${mcpVersionFile} not found. This step expects to run on " +
              "a platform-repo checkout that has been bootstrapped (mcp pin committed).")
    }
    String mcpSha = readFile(mcpVersionFile).trim()
    if (!(mcpSha ==~ /[0-9a-f]{7,40}/)) {
        error("ragRebuildIndex: .rag/mcp-version does not look like a git SHA: '${mcpSha}'")
    }

    String mcpDir = '.jenkins-mcp'
    String venvDir = '.jenkins-rag-venv'

    // Everything from clone → run → commit → push holds the shared lock,
    // so rebuild-vs-ingest can never race on .rag/openai-state.json.
    // Argument parsing and the pin-file sanity checks above run unlocked
    // (they don't touch the state file). The lock is shared with
    // ragIngestDocs (same resource name). Requires the Lockable Resources
    // plugin; without it, Jenkins fails the build with a clear error,
    // which is the desired behavior — silent racing is worse.
    lock(resource: 'platform-rag-state') {

    // ─── 1. Sparse-clone mcp at the pinned SHA ─────────────────────────────
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
    // Reconcile only needs the openai SDK (no chunker deps — no chunking).
    // dry-run uses the fake client and needs neither.
    String pipPackages = dryRun ? "" : "'openai'"
    if (dryRun) {
        sh """#!/usr/bin/env bash
set -euo pipefail
rm -rf '${venvDir}'
python3 -m venv '${venvDir}'
. '${venvDir}'/bin/activate
pip install --quiet --upgrade pip
"""
    } else {
        sh """#!/usr/bin/env bash
set -euo pipefail
rm -rf '${venvDir}'
python3 -m venv '${venvDir}'
. '${venvDir}'/bin/activate
pip install --quiet --upgrade pip
pip install --quiet ${pipPackages}
"""
    }

    // ─── 3. Run the driver ────────────────────────────────────────────────
    // Setup commands run under `set -e`. The python call alone is wrapped
    // with `set +e ... set -e` to capture its exit code, mirroring
    // ragIngestDocs. Unlike ingest, reconcile has no "partial" mode — exit
    // is either 0 (state rewritten) or 2 (setup/listing failure, state
    // mostly untouched). We commit on 0 only.
    if (dryRun) {
        sh """#!/usr/bin/env bash
set -euo pipefail
. '${venvDir}'/bin/activate
set +e
PYTHONPATH='${mcpDir}' python3 '${mcpDir}/tools/rag_rebuild_index.py' \\
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
PYTHONPATH='${mcpDir}' python3 '${mcpDir}/tools/rag_rebuild_index.py' \\
    --platform-root '${platformRootSh}'
RC=\$?
set -e
echo \$RC > .jenkins-rag-rc
"""
        }
    }

    String driverRc = readFile('.jenkins-rag-rc').trim()

    // ─── 4. Commit + push state.json (skipped on dry-run) ────────────────
    if (dryRun) {
        echo "ragRebuildIndex: dry-run — skipping commit + push (driver exit ${driverRc})"
    } else if (driverRc != '0') {
        echo "ragRebuildIndex: driver exit ${driverRc} — skipping commit (state likely untouched)"
    } else {
        dir(platformRoot) {
            sh """#!/usr/bin/env bash
set -euo pipefail

git check-ref-format --branch '${pushBranch}' >/dev/null

git config --local user.name  '${gitUserNameSh}'
git config --local user.email '${gitUserEmailSh}'

git fetch origin '${pushBranch}:refs/remotes/origin/${pushBranch}'

git add .rag/openai-state.json
if git diff --cached --quiet -- .rag/openai-state.json; then
    echo '.rag/openai-state.json unchanged — no commit needed'
    exit 0
fi
git commit -m 'rag: state rebuilt by reconcile'
git rebase 'origin/${pushBranch}' || {
    echo 'rag: rebase onto origin/${pushBranch} failed (likely a true conflict in .rag/)'
    git rebase --abort || true
    exit 1
}
git push 'git@github.com:${githubRepo}' "HEAD:${pushBranch}"
"""
        }
    }

    if (driverRc != '0') {
        error("ragRebuildIndex: driver exited ${driverRc}")
    }

    }  // end lock
}
