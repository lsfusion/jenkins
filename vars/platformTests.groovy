// Runs the platform tests of a branch without deploying anything : for a branch that is not on master yet, or
// for a change to this library, where the deploy job is the wrong instrument. Needs a workspace of its own -
// the deploy jobs share Paths.src, and this one checks out another branch and cleans the tree.
def call(String branch, String tests = null) {
    update branch

    if (!fileExists('tests/compose.yaml')) {
        error "branch $branch has no tests module"
    }

    try {
        sh "rm -rf */target/surefire-reports tests/target/failsafe-reports"
        sh "docker compose -f tests/compose.yaml up -d db --wait"
        // the build the deploy makes, with the unit tests of every module (maven.test.skip) and the tests module's;
        // only nothing is signed, installed or deployed
        sh "mvn -ntp clean verify -Dmaven.test.skip=false -P-sign-desktop-jar" + (tests ? " -Dlsf.tests='$tests'" : "")
    } finally {
        junit allowEmptyResults: true, testResults: '*/target/surefire-reports/*.xml, tests/target/failsafe-reports/*.xml'
        sh "docker compose -f tests/compose.yaml down -v"
    }
}
