def call(String jenkinsHome, String branch) {
    def platform = "${jenkinsHome}/src/${branch}/platform"
    def fsl = "${jenkinsHome}/src/${branch}/fsl"
    def repos = '-DaltReleaseDeploymentRepository=lsfusion::default::http://repo.lsfusion.org/repository/releases -DaltSnapshotDeploymentRepository=lsfusion::default::http://repo.lsfusion.org/repository/snapshots'

    pipeline {
        agent any
        stages {
            update "${jenkinsHome}", "${branch}"

            stage('deploy') {
                steps {
                    sh "mvn -f ${platform}/pom.xml clean"
                    sh "mvn -f ${platform}/base/pom.xml ${repos} deploy -P sources"
                    sh "mvn -f ${platform}/api/pom.xml ${repos} deploy -P sources"
                    sh "mvn -f ${platform}/report-api/pom.xml ${repos} deploy -P sources"
                    sh "mvn -f ${platform}/web-api/pom.xml ${repos} deploy -P sources"
                    sh "mvn -f ${platform}/logics/pom.xml ${repos} deploy -P sources"

                    sh "mvn -f ${fsl}/pom.xml clean"
                    sh "mvn -f ${fsl}/fsl-logics/pom.xml ${repos} deploy -P sources"

                    sh "mvn -f ${platform}/server/pom.xml deploy -P sources,assemble"
                    sh "mvn -f ${fsl}/fsl-server/pom.xml deploy -P sources,assemble"
                    sh "mvn -f ${platform}/desktop-client/pom.xml deploy -P sources,assemble,sign,pack"
                    sh "mvn -f ${platform}/web-client/pom.xml deploy -P sources,gwt,desktop,war"
                }
            }
        }
    }
}
