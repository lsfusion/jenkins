class Paths {
    static String jenkinsHome = '/usr/share/jenkins'
    static String src = "$jenkinsHome/src"
    static String tmp = "/tmp"
    static String download = "/var/www/download"
    static String rpm = "$jenkinsHome/rpm"
    static String apt = "$jenkinsHome/apt"
    
    static String githubRepo = "nomojenkins/platform"

    // for testing purposes
    static String githubIssuesRepo = null
    static boolean noCustomUpdates = false
    static String slackChannel = "#jenkinstest"
    
    static String githubCredentials = "1237e973-dddd-450b-b46d-6b8ef824a1c3"
}
