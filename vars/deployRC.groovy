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
                        branch: 'release-candidate'
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
                    sh "/usr/local/git/bin/git -C ${Paths.jenkinsHome}/rc/platform-RC log -n 1 --pretty=format:\"<logentry>%n<author>%an</author>%n<msg>%s</msg>%n</logentry>\" > ${Paths.jenkinsHome}/rc/platformLatestCommit"
                    sh "mvn -f ${Paths.jenkinsHome}/rc/platform-RC/pom.xml clean deploy"
                }
            }
        }

        post {
            success {
                slackSend channel: '#jenkins',
                        color: 'good',
                        message: "<${env.BUILD_URL}|${currentBuild.fullDisplayName}> успешно завершён.\n```" + getCommitMessage() + "```"
            }
            failure {
                slackSend channel: '#dev',                   
                        color: 'danger',
                        message: "Внимание! <${env.BUILD_URL}|${currentBuild.fullDisplayName}> завершился с ошибкой.\n```" + getCommitMessage() + "```"
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