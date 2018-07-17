def stn(String name) {
        return {
                stage(name) {
                        sayHello "I am am : " + name
                }
        }
}

def call() {
        String[] branches = ['release', 'master']

        echo "Check status"

        pipeline {
                agent any
                stages {
                        stage('one') {
                                steps {
                                        script {
                                                parallel {
                                                        stage('release') {
                                                                sayHello 'I am am ' + 'release'
                                                        }
                                                        stage('master') {
                                                                sayHello 'I am am ' + 'master'
                                                        }
                                                } 
//                                                branches.collectEntries() {
//                                                        ["${it}": stn(it)]
//                                                }
                                        }
                                }
                        }
                }
        }

//        currentBuild.result = 'SUCCESS' //FAILURE to fail
//        return this
}