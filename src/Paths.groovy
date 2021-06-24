class Paths {
    static String jenkinsHome = '/usr/share/jenkins'
    static String src = "$jenkinsHome/src"
    static String tmp = "/tmp"
    static String download = "/var/www/download"
    static String rpm = "$jenkinsHome/rpm"
    static String dnf = "$jenkinsHome/dnf"
    static String apt = "$jenkinsHome/apt"
    static String ssl = "$jenkinsHome/ssl"
    static String docusaurus = "$jenkinsHome/docusaurus"

    static String githubRepo = "nomojenkins/platform"
    static String docusaurusRepo = "lsfusion/docusaurus.git"
    static String docsRepo = "danchanka/platform.git"

    // for testing purposes
    static String githubIssuesRepo = null
    static boolean noCustomUpdates = true
    static String slackChannel = "#jenkinstest"
    
    static String githubCredentials = "1237e973-dddd-450b-b46d-6b8ef824a1c3"
    
    static String updateAssembleVersionsJob = 'updateAssembleVersionsTest'
    static String updateParentVersionsJob = 'updateParentVersionsTest'
}
