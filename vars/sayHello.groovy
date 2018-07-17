// vars/sayHello.groovy
def call(String name = 'human') {
    // Any valid steps can be called from this code, just like in other
    // Scripted Pipeline
//    pipeline {
//        agent any
//        stages {
//            stage('Odd Stage') {
//                steps {
                    echo "Hello, ${name}."
//                }
//            }
//        }
//    }

}