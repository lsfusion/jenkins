// ragValidateManifest — PR-builder step for lsfusion/platform.
//
// Validates the docs FOLDER STRUCTURE on PRs that touch docs/**. Sparse-clones
// the mcp repo at the SHA pinned in platform/.rag/mcp-version, then runs
// `fill.manifest validate` — now a folder-structure + slug-shape check (there is
// no manifest anymore; a doc's category comes from its docs/<lang> subfolder:
// language/paradigm/how-to/brief/rules).
//
// NOTE: the step name is kept so the existing PR-builder job binding keeps
// working; it no longer validates a manifest. Two server-side follow-ups on the
// job config (which lives in Jenkins, not in a repo):
//   - drop the old `docs/manifest.json` path from the stage's `when` trigger;
//   - remove any `checkBootstrap: true` call site — the manifest bootstrap /
//     classify workflow (BOOTSTRAP_DEFAULTS_REVIEWED) is gone.
// The pinned mcp SHA must point at post-folder-migration mcp for the new
// `validate --docs-dir` form to exist.
//
// Call example (from a Jenkinsfile on the platform repo PR-builder job):
//
//   stage('Validate docs structure') {
//     when { changeRequest() }
//     steps { ragValidateManifest() }
//   }
//
// Arguments (all optional):
//   docsDirs:  doc roots to validate (default: ['docs/en', 'docs/ru'])

import static Paths.mcpRepo

def call(Map args = [:]) {
    List<String> docsDirs = args.docsDirs ?: ['docs/en', 'docs/ru']

    if (!fileExists('.rag/mcp-version')) {
        error("ragValidateManifest: .rag/mcp-version not found in workspace. This step expects " +
              "to run on a platform-repo checkout that has been bootstrapped (mcp pin committed).")
    }
    String mcpSha = readFile('.rag/mcp-version').trim()
    if (!(mcpSha ==~ /[0-9a-f]{7,40}/)) {
        error("ragValidateManifest: .rag/mcp-version does not look like a git SHA: '${mcpSha}'")
    }

    String mcpDir = '.jenkins-mcp'

    // Sparse-clone mcp at the pinned SHA — public repo, no creds needed.
    // `--filter=blob:none --no-checkout` pulls just the tree, then sparse-checkout
    // restricts to `fill/` only (~750 KB instead of full mcp).
    dir(mcpDir) {
        deleteDir()
    }
    // Each `sh` block leads with a bash shebang so it runs under bash regardless of
    // whether /bin/sh on the agent is bash or dash. `set -o pipefail` is a bash-ism
    // and dash rejects it.

    sh """#!/usr/bin/env bash
set -euo pipefail
git clone --filter=blob:none --no-checkout https://github.com/${mcpRepo} ${mcpDir}
cd ${mcpDir}
git sparse-checkout init --cone
git sparse-checkout set fill
git checkout ${mcpSha}
"""

    // `fill.manifest validate` is stdlib-only — no pip install needed. One run
    // per doc root; the check verifies every .md sits in a valid category folder
    // with a well-formed slug, and that slugs don't collide across folders.
    for (docsDir in docsDirs) {
        sh """#!/usr/bin/env bash
set -euo pipefail
PYTHONPATH='${mcpDir}' python3 -m fill.manifest validate \\
    --docs-dir '${docsDir}'
"""
    }
}
