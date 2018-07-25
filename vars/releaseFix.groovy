
def call(String branch) {
    stage('Update') {
        steps {
            update branch
        }
    }
    stage('Release branch') {
        steps {
            dir(Paths.src) {
                def nextVersion = lastVersion + 1
                sh "mvn -B release:clean release:prepare release:perform"
            }
        }
    }

    // Next 3 tasks, to local folder
    stage('Build installers') {
        steps {
            // todo            
        }
    }

    stage('Generate JNLP') {
        steps {
            // todo            
        }
    }

    stage('Copy dependencies') {
        steps {
            // todo            
        }
    }
    
    // Upload from local folder to global
    stage('Upload to CDN') {
        steps {
            // todo: https://stackoverflow.com/questions/41174045/using-jenkins2-pipeline-to-upload-via-ftp            
        }
    }

    stage('Change custom assemble versions') {
        steps {
            // todo
        }
    }
}

