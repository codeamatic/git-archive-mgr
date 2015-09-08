package codeamatic.gam.archives.support;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codeamatic.gam.archives.Archive;
import codeamatic.gam.projects.Project;

@Service
public class ArchiveProcessor {

  /**
   * Processes the archiving and comparison of deltas between git commits/branches.
   *
   * @param project populated project object
   * @param archive populated archive object
   * @return a string representation of the temporary directory where the zip archive is located
   */
  public String process(Project project, Archive archive) throws IOException, InterruptedException {

    BufferedReader br = getDiffResults(archive, project.getProjectDirectory());
    String line;

    // Retrieve branch diff
    List<String> appList = new ArrayList<>();
    List<String> webList = new ArrayList<>();
    List<String> otherList = new ArrayList<>();

    String webPrefix = archive.getWebPrefix();
    String appPrefix = archive.getAppPrefix();

    while ((line = br.readLine()) != null) {
      if (line.startsWith(webPrefix)) {
        webList.add(line);
      } else if (line.startsWith(appPrefix)) {
        appList.add(line);
      } else {
        // add to a separate list used for various files that weren't
        // part of the app or web list.  This could be .xml files, config files, source code, etc.
        otherList.add(line);
      }
    }

//    Map<String, Object> deltaMap = new HashMap<>();
//    deltaMap.put("webList", webList);
//    deltaMap.put("appList", appList);
//    deltaMap.put("otherList", otherList);
//
//    return deltaMap;
    String zipPath = generateZips(appList, webList, archive, project);

    return zipPath;
  }

  /**
   * Process and generate zip archives for
   */
  private String generateZips(List<String> appList, List<String> webList, Archive archive, Project project)
      throws IOException {

    // Do nothing if the files haven't changed
    if (appList.isEmpty() && webList.isEmpty()) {
      System.out.println("No files have changed.");
      return null;
    }

    String fromAuthor = "From_" + project.getProjectOwner();

    // Create temp directory used for building archive
    Path
        tempDir =
        Files.createTempDirectory(project.getProjectName().toLowerCase());
    Path prjStructureOut;

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    if (Files.exists(tempDir)) {
      prjStructureOut =
          Paths.get(
              tempDir + "/" + fromAuthor + "/" + project.getProjectName()
              + "/" + simpleDateFormat.format(date));
      Files.createDirectories(prjStructureOut);
    } else {
      // kill it...
      System.out.println("ERROR creating temporary directory.");
      return null;
    }

    final Map<String, String> env = new HashMap<>();
    env.put("create", "true");

    if (!webList.isEmpty()) {
      URI
          uri =
          URI.create("jar:file:/" + prjStructureOut.toString().replace("\\", "/") + "/web.zip");

      System.out.println("\n# Web Updates");

      try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

        for (String file : webList) {
          Path source = Paths.get(project.getProjectDirectory() + "/" + file);

          if (Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          System.out.println(file);

          Path target = zipfs.getPath(file.replace(archive.getWebPrefix(), ""));
          Path parentTargetDir = target.getParent();

          if (parentTargetDir != null && !Files.exists(parentTargetDir)) {
            Files.createDirectories(parentTargetDir);
          }

          Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
      }
    }

    if (!appList.isEmpty()) {
      URI
          uri =
          URI.create("jar:file:/" + prjStructureOut.toString().replace("\\", "/") + "/app.zip");

      System.out.println("# App Updates");

      try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

        for (String file : appList) {
          Path source = Paths.get(project.getProjectDirectory() + "/" + file);

          if (Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          System.out.println(file);

          Path target = zipfs.getPath(file.replace(archive.getAppPrefix(), ""));
          Path parentTargetDir = target.getParent();

          if (parentTargetDir != null && !Files.exists(parentTargetDir)) {
            Files.createDirectories(parentTargetDir);
          }

          Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
      }
    }

    // Create final zip
    URI
        uri =
        URI.create("jar:file:/" + tempDir.toString().replace("\\", "/") + "/" + fromAuthor + "_"
                   + project.getProjectName() + ".zip");

    // Walk the file source and build a zip from it
    String zipPath;

    try (FileSystem zipFinal = FileSystems.newFileSystem(uri, env)) {
      Path source = Paths.get(tempDir + "/" + fromAuthor);
      Path target = zipFinal.getPath(fromAuthor);

      Files.walkFileTree(source, new CopyFileVisitor(target));
      zipPath = zipFinal.toString();
    }

    System.out.println("Complete with diffing");
    return zipPath;
  }

  /**
   * Retrieves the delta's between two git parameters (branches or commits) and returns the results
   * wrapped in a {@link BufferedReader} object.
   *
   * <p>Git commands are executed using the ProcessBuilder so git is expected to be installed and
   * runnable from the command line.
   *
   * @param archive    A populated archive model object
   * @param projectDir The directory of the git root within the project
   * @return a BufferedReader object ready to process the resulting lines
   */
  private BufferedReader getDiffResults(Archive archive, String projectDir)
      throws IOException, InterruptedException {

    // Create [git] fetch process list
    List<String> processFetchList = new ArrayList<>();
    processFetchList.add("git");
    processFetchList.add("fetch");
    processFetchList.add("origin");

    // If this is a commit based comparison as opposed to a branch based
    // comparison, we should do a fetch on only the branch
    if (archive.getDiffBranch() != null) {
      processFetchList.add(archive.getDiffBranch());
    }

    // Build process for [git] fetch
    ProcessBuilder pbFetch = new ProcessBuilder(processFetchList);
    pbFetch.directory(new File(projectDir));
    Process processFetch = pbFetch.start();

    // wait until fetching has been completed
    processFetch.waitFor();

    // Create [git] diff process list
    List<String> processDiffList = new ArrayList<>();
    processDiffList.add("git");
    processDiffList.add("diff");
    processDiffList.add("--name-only");
    processDiffList.add(archive.getDiffParam1());
    processDiffList.add(archive.getDiffParam2());

    // Build process for [git] diff
    ProcessBuilder processBuilder = new ProcessBuilder(processDiffList);
    processBuilder.directory(new File(projectDir));
    Process process = processBuilder.start();

    // Migrate process results into a BufferedReader
    InputStream is = process.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);

    return new BufferedReader(isr);
  }
}