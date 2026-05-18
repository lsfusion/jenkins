@Grab("org.eclipse.jgit:org.eclipse.jgit:7.0.0.202409031743-r")
@Grab("org.eclipse.mylyn.github:org.eclipse.egit.github.core:2.1.5")

def call(String to) {
    try {
        def repoParts = (Paths.githubIssuesRepo == null ? Paths.githubRepo : Paths.githubIssuesRepo).split('/')

        String user = repoParts[0] //mycompany
        String repo = repoParts[1]

        def pathToRepo = pwd()

        def issuesData = getClosedEvents(user, repo)
        def result = ""
        def from = getPrevVersion(to)
        def repoUrl = "https://github.com/${user}/${repo}"

        def commits = getCommitsBetweenTags(pathToRepo, from, "HEAD")
        def issues = getClosedIssues(issuesData, commits)
        def groupedIssues = groupIssuesByType(issues)

        result += genHeader(to, new Date())
        for (def type in ['MAJORENHANCEMENT', 'ENHANCEMENT', 'BUG']) {
            result += genIssuesOfType(type, groupedIssues[type], repoUrl)
        }

        echo result

        def updatedFileContent = insertAfterFirstLine(readFile('CHANGELOG.md'), result)

        writeFile file: 'CHANGELOG.md', text: updatedFileContent

        echo "Changelog generated successfully!"
    } catch (Exception e) {
        echo "Error: ${e.message}"
        currentBuild.result = 'FAILURE'
    }
}

def getPrevVersion(versionString) {
    if (!(versionString =~ /[0-9]+\.[0-9]+/)) {
        error("Version in branch should be *.*")
    }

    def minorVersionString = versionString.substring(versionString.indexOf('.') + 1)
    def majorVersionString = versionString.substring(0, versionString.indexOf('.') + 1)

    def prevVersion
    def minorVersion = Integer.valueOf(minorVersionString)
    if (minorVersion > 0) {
        prevVersion = majorVersionString + "" + (minorVersion - 1)
    } else {
        int prevMajorVersion = Integer.valueOf(versionString.substring(0, versionString.indexOf('.'))) - 1
        if (prevMajorVersion < 6) {
            return null
        }
        prevVersion = prevMajorVersion + ".0"
    }

    return prevVersion
}

def genHeader(version, date) {
    def dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")
    return "## ${version} (${dateFormat.format(date)})\n"
}

def genIssuesOfType(type, issues, repoUrl) {
    if (!issues || issues.isEmpty()) {
        return ""
    }

    def captions = [
            'MAJORENHANCEMENT': 'Implemented major enhancements',
            'ENHANCEMENT': 'Implemented enhancements',
            'BUG': 'Fixed bugs'
    ]

    def result = "### ${captions[type]}:\n"
    for (def issue in issues) {
        result += " - ${issue.title} [#${issue.number}](${repoUrl}/issues/${issue.number})\n"
    }
    return result
}

def groupIssuesByType(issues) {
    def grouped = [
            'MAJORENHANCEMENT': [],
            'ENHANCEMENT': [],
            'BUG': []
    ]

    for (def issue in issues) {
        def issueType = getMainLabel(issue.labels)
        grouped[issueType] << issue
    }

    return grouped
}

def getMainLabel(labels) {
    for (def label in labels) {
        if (label.name == "major feature") {
            return 'MAJORENHANCEMENT'
        }
        if (label.name == "bug") {
            return 'BUG'
        }
    }
    return 'ENHANCEMENT'
}

def getClosedEvents(user, repo) {
    // issueNumber -> [issue: {number, title, labels}, commitIds: Set<String>, closed: bool]
    def issuesData = [:]

    withCredentials([string(credentialsId: 'api.github.com.token', variable: 'TOKEN')]) {
        def perPage = 100
        def page = 1
        while (true) {
            def response = sh(
                    script: """
                    curl -s -H "Authorization: token ${TOKEN}" \
                    "https://api.github.com/repos/${user}/${repo}/issues/events?per_page=${perPage}&page=${page}"
                """,
                    returnStdout: true
            )

            def events = readJSON text: response
            if (events == null || events.isEmpty()) {
                break
            }

            for (def event in events) {
                if (event.event != "closed" && event.event != "referenced") {
                    continue
                }
                if (!event.issue) {
                    continue
                }

                def num = event.issue.number
                def entry = issuesData[num]
                if (entry == null) {
                    entry = [
                            issue: [
                                    number: num,
                                    title: event.issue.title,
                                    labels: event.issue.labels
                            ],
                            commitIds: [] as Set,
                            closed: false
                    ]
                    issuesData[num] = entry
                }
                if (event.event == "closed") {
                    entry.closed = true
                }
                if (event.commit_id) {
                    entry.commitIds << event.commit_id
                }
            }

            if (events.size() < perPage) {
                break
            }
            page++
        }
    }

    return issuesData
}

def getClosedIssues(issuesData, commits) {
    def closedIssues = []
    for (def entry in issuesData.values()) {
        if (!entry.closed) {
            continue
        }
        if (entry.commitIds.any { commits.contains(it) }) {
            closedIssues << entry.issue
        }
    }
    return closedIssues
}

def getCommitsBetweenTags(pathToRepo, tag1, tag2) {
    def commits = []

    def tag1Exists = sh(
            script: "git rev-parse --verify ${tag1} >/dev/null 2>&1 && echo 'exists' || echo 'not exists'",
            returnStdout: true
    ).trim() == 'exists'

    def tag2Exists = sh(
            script: "git rev-parse --verify ${tag2} >/dev/null 2>&1 && echo 'exists' || echo 'not exists'",
            returnStdout: true
    ).trim() == 'exists'

    if (!tag1Exists || !tag2Exists) {
        echo "Skipping - one or both tags don't exist"
        return commits // возвращаем пустой список
    }

    dir(pathToRepo) {
        // Получаем коммиты между тегами используя нативный git
        def commitList = sh(
                script: """
                git log --pretty=format:"%H" ${tag1}..${tag2}
            """,
                returnStdout: true
        ).trim()

        commits = commitList.split('\n') as List
    }

    return commits as Set
}

def getTagDate(pathToRepo, tag) {
    def dateStr = ""

    dir(pathToRepo) {
        dateStr = sh(
                script: """
                git log -1 --format=%ai ${tag}
            """,
                returnStdout: true
        ).trim()
    }

    def format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return format.parse(dateStr)
}

def insertAfterFirstLine(existingContent, newContent) {
    def lines = existingContent.split('\n')
    def resultLines = []

    if (lines.size() > 0) {
        resultLines << lines[0]
    }

    resultLines << ""
    resultLines << newContent.trim()
    resultLines << ""

    for (int i = 1; i < lines.size(); i++) {
        resultLines << lines[i]
    }

    return resultLines.join('\n')
}