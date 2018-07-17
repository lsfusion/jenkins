def call(String jenkinsHome, String branch) {
    pipeline {
        agent any
        stages {
            stage('update') {
                steps {
                    dir("${jenkinsHome}/sources") {
                        dir('fsl') {
                            git(
                                    url: 'https://github.com/lsfusion/fsl',
                                    branch: "${branch}"
                            )
                        }

                        dir('platform') {
                            git(
                                    url: 'https://github.com/lsfusion/platform',
                                    branch: "${branch}"
                            )
                        }
                    }
                }
            }
        }
    }
}