def call() {
    properties([
            pipelineTriggers([
                    [$class: "SCMTrigger", scmpoll_spec: "* * * * *"],
            ])
    ])

    node {
        stage('pull') {
            dir("${Paths.jenkinsHome}/rc/platform-RC") {
                git(
                        poll: true,
                        url: 'https://github.com/lsfusion/platform',
                        branch: 'v2'
                )
            }
        }
    }

    pipeline {
        agent none
        stages {
            stage('single') {
                agent any
                steps {
                    dir("${Paths.jenkinsHome}/rc/platform-RC") {
                        sh "git log -n 1 --pretty=format:\"<logentry>%n<author>%an</author>%n<msg>%s</msg>%n</logentry>\" > ../platformLatestCommit"
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
                    slack.error "Внимание! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> завершился с ошибкой.\n```" + getCommitMessage() + "```", '#dev'
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