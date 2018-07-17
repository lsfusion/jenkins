def stn(String name) {
        return {
                stage(name) {
                        steps {
                                sayHello "I am am : " + name
                        }
                }
        }
}

def call() {
        echo "Check status"

        pipeline {
                agent any
                stages {
                        stage('one') {
                                parallel ['release', 'master'].collectEntries()
                                        ["dd ${it}": stn(it)]
                        }
                }
        }

//        currentBuild.result = 'SUCCESS' //FAILURE to fail
//        return this
}