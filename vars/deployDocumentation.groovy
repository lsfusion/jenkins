import static Paths.documentation

def call(String commitMessage) {
    dir(documentation) {
        git(
                url: "git@github.com:$Paths.documentationRepo",
                branch: 'master'
        )
        
        sh 'yarn install'

        def list = readJSON text: new File("$documentation/update.config.json").text
        list.each {
            copyDocPages(it.id, it.branch)
        }

        sh "git add -A"
        sh "git diff-index --quiet HEAD || git commit -m 'Updated doc pages'"
        sh "git push origin master"

        sh "GIT_USER=nomojenkins USE_SSH=true DEPLOYMENT_BRANCH=master yarn deploy"

        if (commitMessage != null)
            slack.message "Documentation has been successfully updated.\n```" + commitMessage + "```"
    }
}

def copyDocPages(id, branch) {
    def isNext = "next".equals(id)
    def enPath = isNext ? "$documentation/docs" : "$documentation/versioned_docs/version-$id"
    def ruPath = isNext ? "$documentation/i18n/ru/docusaurus-plugin-content-docs/current" : "$documentation/i18n/ru/docusaurus-plugin-content-docs/version-$id"

    dir("$Paths.src") {
        git(
                url: "git@github.com:$Paths.docsRepo",
                branch: branch
        )

        dir("docs") {
            sh "rm -rf $enPath"
            sh "cp -fa en $enPath"

            sh "rm -rf $ruPath"
            sh "cp -fa ru $ruPath"

            // The current-docs navigation lives in platform/docs/sidebars.js
            // (beside en/ru, so the copies above don't touch it). Materialize it
            // for the build as docusaurus/sidebars.generated.js, which the repo's
            // sidebars.js loader falls back to. Versioned docs keep their own
            // versioned_sidebars/*.json, so this runs for the current version only.
            if (isNext) {
                sh "cp -fa sidebars.js $documentation/sidebars.generated.js"
            }
        }
    }
}