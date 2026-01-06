class Paths {
    static String jenkinsHome = '/usr/share/jenkins'
    static String src = "$jenkinsHome/lsFusion/mycompany"
    static String tmp = "/tmp"
    static String download = "/var/www/download"
    static String rpm = "$jenkinsHome/rpm"
    static String apt = "$jenkinsHome/apt"
    static String ssl = "$jenkinsHome/ssl"
    static String signedDir = "$ssl/signed"
    static String documentation = "$jenkinsHome/docs"
    static String antlr4 = "/antlr"
    static String lsfLogics = "server/src/main/antlr3/lsfusion/server/language/LsfLogics.g"

    static String githubRepo = "lsfusion-solutions/mycompany.git"
    static String documentationRepo = "lsfusion-solutions/mycompany-docs.git"
    static String docsRepo = "lsfusion-solutions/mycompany.git"

    // for testing purposes
    static String githubIssuesRepo = null
    static boolean noCustomUpdates = true
    static String slackChannel = null
    
    static String githubCredentials = "1237e973-dddd-450b-b46d-6b8ef824a1c3"
    
    static String updateAssembleVersionsJob = 'updateAssembleVersions'
    static String updateParentVersionsJob = 'updateParentVersions'
}
