def stn(String name) {
        return {
                stage(name) {
                        steps {
                                sayHello "I am am : " + name
                        }
                }
        }
}

String[] branches = ['release', 'master']
def call() {
        echo "Check status"

        pipeline {
                agent any
                stages {
                        stage('one') {
                                steps {
                                        script {
                                                parallel branches.collectEntries()
                                                ["dd ${it}": stn(it)]
                                        }
                                }
                        }
                }
        }

//        currentBuild.result = 'SUCCESS' //FAILURE to fail
//        return this
}