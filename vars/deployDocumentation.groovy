import static Paths.documentation

def call() {
    call("")
}

def call(String commitMessage) {
    dir(documentation) {
        git(
                url: "git@github.com:$Paths.documentationRepo",
                branch: 'main'
        )
        
        sh 'yarn install'

//        def list = readJSON text: new File("$documentation/update.config.json").text
//        list.each {
//            copyDocPages(it.id, it.branch)
//        }
        copyDocPages("next", "v6")


        sh "git add -A"
        sh "git diff-index --quiet HEAD || git commit -m 'Updated doc pages'"
        sh "git push origin main"

        sh "GIT_USER=nomojenkins USE_SSH=true DEPLOYMENT_BRANCH=main yarn deploy"

//        if (commitMessage != null)
//            slack.message "Documentation has been successfully updated.\n```" + commitMessage + "```"
    }
}

def copyDocPages(id, branch) {
    def isNext = "next".equals(id)
    def enPath = isNext ? "$documentation/docs" : "$documentation/versioned_docs/version-$id"
    def ruPath = isNext ? "$documentation/i18n/ru/docusaurus-plugin-content-docs/current" : "$documentation/i18n/ru/docusaurus-plugin-content-docs/version-$id"
    def plPath = isNext ? "$documentation/i18n/pl/docusaurus-plugin-content-docs/current" : "$documentation/i18n/pl/docusaurus-plugin-content-docs/version-$id"

    dir("$Paths.src") {
        git(
                url: "git@github.com:$Paths.docsRepo",
                branch: branch
        )

        dir("src/main/resources/docs") {
//            sh "rm -rf $enPath"
//            sh "cp -fa en $enPath"
//
//            sh "rm -rf $ruPath"
//            sh "cp -fa ru $ruPath"
            
            sh "rsync -a en/ $enPath/"
            sh "rsync -a ru/ $ruPath/"
            sh "rsync -a pl/ $plPath/"
        }
    }
}