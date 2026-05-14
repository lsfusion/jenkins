// ragValidateManifest — PR-builder step for lsfusion/platform.
//
// Runs on PRs that touch docs/en/** or docs/manifest.json. Sparse-clones
// the mcp repo at the SHA pinned in platform/.rag/mcp-version, installs the
// `fill` package, and runs `fill.manifest validate` against the PR checkout.
//
// Optional bootstrap acceptance check (when `checkBootstrap: true` is passed)
// also runs `fill.manifest check-bootstrap` to enforce the
// BOOTSTRAP_DEFAULTS_REVIEWED:<N> marker contract for bootstrap PRs.
//
// Call examples (from a Jenkinsfile on the platform repo PR-builder job):
//
//   stage('Validate manifest') {
//     when { changeRequest() }
//     steps { ragValidateManifest() }
//   }
//
//   // Bootstrap PR (touching .rag/bootstrap/):
//   steps {
//     ragValidateManifest(
//       checkBootstrap: true,
//       prDescription: env.CHANGE_BODY,  // GitHub PR body, populated by Jenkins
//     )
//   }
//
// Arguments (all optional, sensible defaults):
//   manifestPath:    path to manifest.json     (default: 'docs/manifest.json')
//   docsDir:         path to docs/en           (default: 'docs/en')
//   reportPath:      bootstrap-report.md path  (default: '.rag/bootstrap/bootstrap-report.md')
//   acceptanceFile:  acceptance.md path        (default: '.rag/bootstrap/acceptance.md')
//   checkBootstrap:  run BOOTSTRAP_DEFAULTS_REVIEWED marker check (default: false)
//   prDescription:   raw PR body text for marker scan (default: '')

import static Paths.mcpRepo

def call(Map args = [:]) {
    String manifestPath   = args.manifestPath   ?: 'docs/manifest.json'
    String docsDir        = args.docsDir        ?: 'docs/en'
    String reportPath     = args.reportPath     ?: '.rag/bootstrap/bootstrap-report.md'
    String acceptanceFile = args.acceptanceFile ?: '.rag/bootstrap/acceptance.md'
    Boolean checkBootstrap = args.checkBootstrap ?: false
    String prDescription   = args.prDescription  ?: ''

    if (!fileExists('.rag/mcp-version')) {
        error("ragValidateManifest: .rag/mcp-version not found in workspace. This step expects " +
              "to run on a platform-repo checkout that has been bootstrapped (mcp pin committed).")
    }
    String mcpSha = readFile('.rag/mcp-version').trim()
    if (!(mcpSha ==~ /[0-9a-f]{7,40}/)) {
        error("ragValidateManifest: .rag/mcp-version does not look like a git SHA: '${mcpSha}'")
    }

    String mcpDir = '.jenkins-mcp'
    String venvDir = '.jenkins-venv'

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

    // Fresh venv per build — Jenkins workspace may persist between runs.
    sh """#!/usr/bin/env bash
set -euo pipefail
rm -rf ${venvDir}
python3 -m venv ${venvDir}
. ${venvDir}/bin/activate
pip install --quiet --upgrade pip
pip install --quiet -e ${mcpDir}/fill
"""

    // Validate manifest schema + consistency.
    sh """#!/usr/bin/env bash
set -euo pipefail
. ${venvDir}/bin/activate
python3 -m fill.manifest validate \\
    --manifest '${manifestPath}' \\
    --docs-dir '${docsDir}'
"""

    if (checkBootstrap) {
        // PR_BODY env keeps potentially-multiline PR descriptions out of the shell.
        withEnv(["PR_BODY=${prDescription}"]) {
            sh """#!/usr/bin/env bash
set -euo pipefail
. ${venvDir}/bin/activate
python3 -m fill.manifest check-bootstrap \\
    --report '${reportPath}' \\
    --acceptance-file '${acceptanceFile}' \\
    --pr-description "\$PR_BODY"
"""
        }
    }
}
