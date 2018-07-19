def call() {
    properties([
            pipelineTriggers([
                    [$class: "SCMTrigger", scmpoll_spec: "* * * * *"],
            ])
    ])

    node {
        stage('pull') {
            dir("${Paths.jenkinsHome}/rc") {
                dir('platform-RC') {
                    git(
                            poll: true,
                            url: 'https://github.com/lsfusion/platform',
                            branch: 'release-candidate'
                    )
                }
                dir('fsl-RC') {
                    git(
                            poll: true,
                            url: 'https://github.com/lsfusion/fsl',
                            branch: 'release-candidate'
                    )
                }
            }
        }
    }

    pipeline {
        agent none
        stages {
            stage('single') {
                agent any
                steps {
                    build 'saveLatestRCCommitToFile'
                    build 'cleanRC'
                    sh "mvn -f ${Paths.jenkinsHome}/rc/platform-RC/pom.xml deploy"
                    sh "mvn -f ${Paths.jenkinsHome}/rc/fsl-RC/pom.xml deploy"
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
    def datePlatform = Date.parse('yyyy-MM-dd HH:mm:ss ZZ', logPlatform.date.text())

    def logFsl = new XmlSlurper().parse("${Paths.jenkinsHome}/rc/fslLatestCommit")
    assert logFsl instanceof groovy.util.slurpersupport.GPathResult
    def dateFsl = Date.parse('yyyy-MM-dd HH:mm:ss ZZ', logFsl.date.text())

    if (datePlatform >= dateFsl)  {
        return logPlatform.msg.text() + '\nby ' + logPlatform.author.text()
    } else {
        return logFsl.msg.text() + '\nby ' + logFsl.author.text()
    }
}