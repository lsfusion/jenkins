def call(String platformVersion) {
    def downloadDir = "${Paths.download}/${platformVersion}"
    sh "mkdir -p ${downloadDir}"
    
    sh "cp -fa server/src/main/resources/client.jnlp ${downloadDir}/lsfusion-client-${platformVersion}.jnlp"

    sh """sed -i -e 's|\${jnlp.codebase}|https://download.lsfusion.org/java|' \\
-e "s/\$\"{jnlp.url}\"/lsfusion-client-${platformVersion}.jnlp/" \\
-e 's|\${jnlp.appName}|lsFusion|' \\
-e 's|\${jnlp.initHeapSize}|32m|' \\
-e 's|\${jnlp.maxHeapSize}|800m|' \\
-e 's|\${jnlp.minHeapFreeRatio}|30|' \\
-e 's|\${jnlp.maxHeapFreeRatio}|70|' \\
-e 's|\${jnlp.vmargs}||' \\
-e 's|\${jnlp.host}|localhost|' \\
-e 's|\${jnlp.port}|7652|' \\
-e 's|\${jnlp.exportName}|default|' \\
-e 's|\${jnlp.singleInstance}|false|' \\
-e "s|lsfusion-client.jar|lsfusion-client-${platformVersion}.jar|" ${downloadDir}/lsfusion-client-${platformVersion}.jnlp"""
}

