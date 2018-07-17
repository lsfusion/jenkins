def call() {
        echo "Check status"

        (1..3).each {
                sayHello "I am am : " + it 
        }

//        currentBuild.result = 'SUCCESS' //FAILURE to fail
//        return this
}