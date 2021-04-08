import static Paths.docusaurus

node {
    dir(docusaurus) {
        git(
                url: "git@github.com:$Paths.docusaurusRepo",
                branch: 'master'
        )

        def list = readJSON text: new File("$docusaurus/update.config.json").text
        list.each {
            copyDocPages(it.id, it.branch)
        }

        sh "git add -A"
        sh "git diff-index --quiet HEAD || git commit -m 'Updated doc pages'"
        sh "git push origin master"

        sh "GIT_USER=nomojenkins USE_SSH=true yarn deploy"
    }
}

def copyDocPages(id, branch) {
    def isNext = "next".equals(id)
    def enPath = isNext ? "$docusaurus/docs" : "$docusaurus/versioned_docs/version-$id"
    def ruPath = isNext ? "$docusaurus/i18n/ru/docusaurus-plugin-content-docs/current" : "$docusaurus/i18n/ru/docusaurus-plugin-content-docs/version-$id"

    dir("/usr/share/jenkins/src_docs") {
        git(
                url: 'git@github.com:danchanka/platform.git',
                branch: branch
        )

        dir("docs") {
            sh "rm -rf $enPath"
            sh "cp -fa en $enPath"

            sh "rm -rf $ruPath"
            sh "cp -fa ru $ruPath"
        }
    }
}