@Grab("org.eclipse.jgit:org.eclipse.jgit:5.0.2.201807311906-r")
@Grab("org.eclipse.mylyn.github:org.eclipse.egit.github.core:2.1.5")

import org.eclipse.egit.github.core.Issue
import org.eclipse.egit.github.core.IssueEvent
import org.eclipse.egit.github.core.Label
import org.eclipse.egit.github.core.service.IssueService
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

import java.text.SimpleDateFormat

def call(String to) {
    // in theory it should be read from scm tag in pom.xml, but it's not that easy task
    def repoParts = (Paths.githubIssuesRepo == null ? Paths.githubRepo : Paths.githubIssuesRepo).split('/')
    
    String user = repoParts[0] //lsFusion
    String repo = repoParts[1]
    
    def pathToRepo = pwd()

    List<IssueEvent> closedEvents = getClosedEvents(user, repo)

    boolean head = true;
    String result = "";
    String shortResult = ""
    String from;
    String url = "https://github.com/" + user + "/" + repo

    from = getPrevVersion(to)
    while (from != null) {
        Set<String> commits = getCommitsBetweenTags(pathToRepo, from, head ? "HEAD" : to)

        List<Issue> issues = getClosedIssues(closedEvents, commits)

        def groupedIssues = issues.groupBy { issue -> getMainLabel(issue.labels) }
        result += genHeader(to, head ? new Date() : getTagDate(pathToRepo, to));
        for (def type : IssueType.values())
            result += genIssuesOfType(type, groupedIssues.get(type), url)

        to = from

        if (head) {
            head = false
            shortResult = result
            result += "## ----- PREVIOUS RELEASES --------\n"
        }

        from = getPrevVersion(to)
    }

    writeFile file:'CHANGELOG.md', text:result
    return shortResult
}

static String getPrevVersion(String versionString) {
    if (!(versionString =~ /[0-9]+\.0-beta[0-9]+/ || versionString =~ /[0-9]+\.[0-9]+/))
        error("Version should be either *.0-beta* or *.*")

    String minorVersionString
    String majorVersionString
    if (versionString =~ /[0-9]+\.0-beta[0-9]+/) {
        minorVersionString = versionString.substring(versionString.indexOf('beta') + 4)
        majorVersionString = versionString.substring(0, versionString.indexOf('beta') + 4)
    } else {
        minorVersionString = versionString.substring(versionString.indexOf('.') + 1)
        majorVersionString = versionString.substring(0, versionString.indexOf('.') + 1) // with '.' at the end
    }

    String prevVersion
    def minorVersion = Integer.valueOf(minorVersionString)
    if(minorVersion > 0)
        prevVersion = majorVersionString + "" + (minorVersion - 1)
    else {
        int prevMajorVersion = Integer.valueOf(versionString.substring(0, versionString.indexOf('.'))) - 1
        if(prevMajorVersion < 2)
            return null
        prevVersion = prevMajorVersion + ".0"
    }

    return prevVersion
}

static String genHeader(String to, Date date) {
    return "## " + to + " (" + (new SimpleDateFormat("yyyy-MM-dd").format(date)) + ")\n"
}

static String genIssuesOfType(IssueType type, List<Issue> issues, String repoName) {
    if(issues == null || issues.isEmpty())
        return ""
    String result = "##### " + type.getCaption() + ":\n"
    for(def issue : issues)
        result += " - " + issue.title + " [#" + issue.number +  "](" + repoName + "/issues/" + issue.number + ")\n"
    return result
}

enum IssueType {
    MAJORENHANCEMENT, ENHANCEMENT, BUG

    String getCaption() {
        switch (this) {
            case MAJORENHANCEMENT:
                return "Implemented major enhancements"
            case ENHANCEMENT:
                return "Implemented enhancements"
            case BUG:
                return "Fixed bugs"
        }
        throw new UnsupportedOperationException();
    }
}

static IssueType getMainLabel(List<Label> labels) {
    for(def label : labels) {
        if (label.getName().equals("major feature"))
            return IssueType.MAJORENHANCEMENT
        if (label.getName().equals("bug"))
            return IssueType.BUG
    }
    return IssueType.ENHANCEMENT
}

List<IssueEvent> getClosedEvents(String user, String repo) {
    List<IssueEvent> closedEvents = new ArrayList<>();
    def service = new IssueService()
    for(Collection<IssueEvent> issueEvents : service.pageEvents(user,repo, Integer.MAX_VALUE))
        for(IssueEvent issueEvent : issueEvents) {
            // if commit closes issue
            if(issueEvent.commitId != null && !issueEvent.commitId.isEmpty() && issueEvent.event.equals("closed"))
                closedEvents.add(issueEvent);
        }
    return closedEvents
}

static List<Issue> getClosedIssues(List<IssueEvent> closedEvents, Set<String> commits) {
    List<Issue> closedIssues = new ArrayList<>();
    for(IssueEvent issueEvent : closedEvents) {
        // if commit in required commit list
        if(commits.contains(issueEvent.commitId))
            closedIssues.add(issueEvent.issue);
    }
    return closedIssues
}

static getCommitsBetweenTags(String pathToRepo, String tag1, String tag2) {
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.setMustExist(true);
    def file = new File(pathToRepo, ".git")
    repositoryBuilder.setGitDir(file);
    Repository repository = repositoryBuilder.build();

    Ref from = repository.findRef(tag1)
    Ref to = repository.findRef(tag2)
    def command = new Git(repository).log()
    def fromId = getActualRefObjectId(repository, from)
    def toId = getActualRefObjectId(repository, to)

    command = command.addRange(fromId, toId)
    Set<String> commits = new HashSet<>();
    for (RevCommit commit : command.call())
        commits.add(commit.getName());
    return commits;
}

static getTagDate(String pathToRepo, String tag) {
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.setMustExist(true);
    def file = new File(pathToRepo, ".git")
    repositoryBuilder.setGitDir(file);
    Repository repository = repositoryBuilder.build();

    Ref to = repository.findRef(tag)
    RevWalk walk = new RevWalk(repository);
    return walk.parseTag(to.getObjectId()).getTaggerIdent().getWhen()
}

static ObjectId getActualRefObjectId(Repository repo, Ref ref) {
    final Ref repoPeeled = repo.getRefDatabase().peel(ref);
    if (repoPeeled.getPeeledObjectId() != null) {
        return repoPeeled.getPeeledObjectId();
    }
    return ref.getObjectId();
}
