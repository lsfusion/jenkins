def call() {
    pipeline {
        agent any
        stages {
            stage('pull v2') {
                steps {
                    dir("${Paths.jenkinsHome}/rc/platform-RC") {
                        git(
                                url: 'https://github.com/lsfusion/platform',
                                branch: 'v2'
                        )
                    }
                }
            }
            stage('deploy v2') {
                steps {
                    dir("${Paths.jenkinsHome}/rc/platform-RC") {
                        sh "git log -n 1 --pretty=format:\"<logentry>%n<author>%an</author>%n<msg>%s</msg>%n</logentry>\" > ../platformLatestCommit"
                        sh "mvn clean deploy"
                    }
                }
            }

            stage('pull master') {
                steps {
                    dir("${Paths.jenkinsHome}/rc/platform-RC") {
                        git(
                                url: 'https://github.com/lsfusion/platform',
                                branch: 'master'
                        )
                    }
                }
            }
            stage('deploy master') {
                steps {
                    dir("${Paths.jenkinsHome}/rc/platform-RC") {
                        sh "mvn clean deploy"
                    }
                }
            }
        }

        post {
            success {
                script {
                    slack.message "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> успешно завершён.\n```" + getCommitMessage() + "```"
                }
            }
            failure {
                script {
                    slack.error "Warning! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> завершился с ошибкой.\n```" + getCommitMessage() + "```", '#dev'
                }
            }
        }
    }
}

@NonCPS
def getCommitMessage() {
    def logPlatform = new XmlSlurper().parse("${Paths.jenkinsHome}/rc/platformLatestCommit")
    assert logPlatform instanceof groovy.util.slurpersupport.GPathResult
    return logPlatform.msg.text() + '\nby ' + logPlatform.author.text()
}