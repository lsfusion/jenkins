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
// Required Jenkins credentials (configure on the job):
//   - openai-api-key       (Secret text): OpenAI API key.
//   - rag-vector-store-id  (Secret text): target Vector Store id (vs_...).
//     Only consulted on the first run; persisted into state.json thereafter.
//
// Call example (from a Jenkinsfile on the platform master post-merge job):
//
//   @Library('lsfusion') _
//   pipeline {
//     agent any
//     options { disableConcurrentBuilds() }
//     triggers { githubPush() }
//     stages {
//       stage('Checkout') { steps { checkout scm } }
//       stage('Ingest')   { steps { ragIngestDocs() } }
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
    String pipPackages = dryRun
        ? "'langchain-text-splitters' 'tiktoken' 'python-frontmatter'"
        : "'openai' 'langchain-text-splitters' 'tiktoken' 'python-frontmatter'"
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
}
