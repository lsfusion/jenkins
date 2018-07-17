// vars/releaseFixes.groovy
def call() {
    // Any valid steps can be called from this code, just like in other
    // Scripted Pipeline
        pipeline {
                agent any
                stages {
                        stage('Odd Stage2') {
                                steps {
                                        echo "The build number is odd"
                                }
                        }
                }
        }
//        sayHello 'Jddd'
}