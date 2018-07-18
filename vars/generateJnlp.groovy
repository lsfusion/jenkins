def call(String platformVersion) {
    sh "cp -fa ${Paths.src}/platform/web-api/src/main/resources/client.jnlp ${Paths.tmp}/client-${platformVersion}.jnlp"

    sh """sed -i -e 's|${jnlp.codebase}|http://download.lsfusion.org|' \\
-e "s/\${jnlp.url}/client-${platformVersion}.jnlp/" \\
-e 's|${jnlp.appName}|lsFusion|' \\
-e 's|${jnlp.initHeapSize}|256m|' \\
-e 's|${jnlp.maxHeapSize}|1024m|' \\
-e 's|${jnlp.maxHeapFreeRatio}|70|' \\
-e 's|${jnlp.vmargs}||' \\
-e 's|${jnlp.registryHost}|localhost|' \\
-e 's|${jnlp.registryPort}|7652|' \\
-e 's|${jnlp.exportName}|default|' \\
-e 's|${jnlp.singleInstance}|false|' \\
-e "s|lsfusion-client.jar|lsfusion-client-${platformVersion}.jar|" ${Paths.tmp}/client-${platformVersion}.jnlp"""
}

