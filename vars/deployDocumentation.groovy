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
            sh "rm -rf $ruPath"

            if (isNext) {
                // Current docs use the TYPE-FIRST source layout docs/<type>/{en,ru}/.
                // Reconstruct the flat per-locale tree Docusaurus expects — en under
                // docs/, ru under i18n/ — so the published output is byte-identical to
                // the old layout. The per-type docs/<type>/AGENTS.md sit ABOVE <type>/en
                // and are therefore NOT copied (no stray built pages / llms twins).
                sh "mkdir -p $enPath $ruPath"
                for (t in ['language', 'paradigm', 'how-to', 'brief', 'rules', 'images']) {
                    sh "cp -fa $t/en $enPath/$t"
                    sh "cp -fa $t/ru $ruPath/$t"
                }
                // Current-docs navigation: platform/docs/sidebars.js → materialize
                // it for the build as docusaurus/sidebars.generated.js (the repo's
                // sidebars.js loader falls back to it).
                sh "cp -fa sidebars.js $documentation/sidebars.generated.js"
            } else {
                // v4/v5/v6 keep the OLD flat layout docs/{en,ru}/ on their branches.
                sh "cp -fa en $enPath"
                sh "cp -fa ru $ruPath"
            }
        }
    }
}