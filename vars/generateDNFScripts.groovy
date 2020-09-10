def call(int majorVersion) {
    def downloadDir = "${Paths.download}/dnf"
    
    def templatesDir = getResourcesDir() + '/installer/dnf/scripts'
    def serverName = "lsfusion$majorVersion-server"
    def clientName = "lsfusion$majorVersion-client"

    dir(Paths.dnf) {
        sh "sed 's/<lsfusion-server>/$serverName/g; s/<lsfusion-client>/$clientName/g' $templatesDir/install-lsfusion > install-lsfusion$majorVersion"

        sh "mkdir -p ${downloadDir}"
        sh "cp -fa * ${downloadDir}/"
    }
}