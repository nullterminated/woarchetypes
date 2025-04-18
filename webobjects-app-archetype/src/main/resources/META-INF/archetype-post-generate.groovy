import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/*
Adding .gitignore directly via maven resources plugin is currently broken.
See: https://issues.apache.org/jira/projects/ARCHETYPE/issues/ARCHETYPE-505
Thus, we include the file with a different filename and rename it manually with this post processing script.
See: https://maven.apache.org/archetype/maven-archetype-plugin/advanced-usage.html
*/

Path gitIgnorePath = Paths.get(request.outputDirectory, request.artifactId, "gitignore");
Path targetPath = gitIgnorePath.getParent().resolve(".gitignore");

boolean isArchetypeBuild = gitIgnorePath.toString().contains("it-basic");
boolean setupFailed = false;
errMsg = "";

if (Files.exists(gitIgnorePath)) {
    System.out.println("Now setting up .gitignore file..");
    try {
        Files.move(gitIgnorePath, targetPath);
    } catch (Exception e) {
        errMsg = "[WARNING] Could not setup .gitignore file. Reason: " + e.getClass().getSimpleName() + ": " + e.getMessage();
        setupFailed = true;
    }
} else {
    errMsg = "[WARNING] Could not find $gitIgnorePath! Your project will not have a proper .gitignore file :(";
    setupFailed = true;
}

if (setupFailed) {
    System.err.println(errMsg);
    if (isArchetypeBuild) {
        throw new Exception("Stopping the build because the post processing script ran into a problem. See log for error details.");
    }
}
