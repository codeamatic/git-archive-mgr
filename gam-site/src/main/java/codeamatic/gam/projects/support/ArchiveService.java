package codeamatic.gam.projects.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

import codeamatic.gam.projects.Archive;
import codeamatic.gam.projects.Project;
import codeamatic.gam.util.GitUtil;

@Service
public class ArchiveService {

  private final ArchiveFormAdapater archiveFormAdapater;

  @Autowired
  public ArchiveService(ArchiveFormAdapater archiveFormAdapater) {
    this.archiveFormAdapater = archiveFormAdapater;
  }

  public Archive addArchive(ArchiveForm archiveForm) {
    return archiveFormAdapater.createArchiveFromArchiveForm(archiveForm);
  }

  /**
   * Processes the archiving and comparison of deltas between git commits/branches.
   *
   * @param archive populated archive object
   * @return a string representation of the temporary directory where the zip archive is located
   */
  public String process(Archive archive) throws IOException, InterruptedException {
    Project project = archive.getProject();
    BufferedReader br = getDiffResults(archive, project.getBuildDirectory());
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

    return generateZips(appList, webList, archive, project);
  }

  /**
   * Process and generate zip archives for
   */
  private String generateZips(List<String> appList, List<String> webList, Archive archive,
                              Project project)
      throws IOException {

    // Do nothing if the files haven't changed
    if (appList.isEmpty() && webList.isEmpty()) {
      System.out.println("No files have changed.");
      return null;
    }

    String fromAuthor = "From_" + project.getOwner();

    // Create temp directory used for building archive
    Path
        tempDir =
        Files.createTempDirectory(project.getName().toLowerCase());
    Path prjStructureOut;

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    if (Files.exists(tempDir)) {
      prjStructureOut =
          Paths.get(
              tempDir + "/" + fromAuthor + "/" + project.getName()
              + "/" + simpleDateFormat.format(date));
      Files.createDirectories(prjStructureOut);
    } else {
      // kill it...
      System.out.println("ERROR creating temporary directory.");
      return null;
    }

    final Map<String, String> env = new HashMap<>();
    env.put("create", "true");

    String readmeTxt = archive.getReadmeTxt();

    if (!webList.isEmpty()) {
      URI
          uri =
          URI.create("jar:file:/" + prjStructureOut.toString().replace("\\", "/") + "/web.zip");

      readmeTxt += "\r\n" + "# Web Updates" + "\r\n";

      try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

        for (String file : webList) {
          Path source = Paths.get(project.getBuildDirectory() + "/" + file);

          if (Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          // replace the prefix
          file = file.replace(archive.getWebPrefix(), "");

          readmeTxt += file + "\r\n";

          Path target = zipfs.getPath(file);
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

      readmeTxt += "\r\n" + "# App Updates" + "\r\n";

      try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

        for (String file : appList) {
          Path source = Paths.get(project.getBuildDirectory() + "/" + file);

          if (Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          file = file.replace(archive.getAppPrefix(), "");

          readmeTxt += file + "\r\n";

          Path target = zipfs.getPath(file);
          Path parentTargetDir = target.getParent();

          if (parentTargetDir != null && !Files.exists(parentTargetDir)) {
            Files.createDirectories(parentTargetDir);
          }

          Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
      }
    }

    // Write readme data to README txt file
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(prjStructureOut.toString().replace("\\", "/") + "/README.txt"),
        "utf-8"))) {
      writer.write(readmeTxt);
    }

    // Create final zip
    URI
        uri =
        URI.create("jar:file:/" + tempDir.toString().replace("\\", "/") + "/" + fromAuthor + "_"
                   + project.getName() + ".zip");

    // Walk the file source and build a zip from it
    String zipPath;

    try (FileSystem zipFinal = FileSystems.newFileSystem(uri, env)) {
      Path source = Paths.get(tempDir + "/" + fromAuthor);
      Path target = zipFinal.getPath(fromAuthor);

      Files.walkFileTree(source, new CopyFileVisitor(target));
      zipPath = zipFinal.toString();
    }

    System.out.println(readmeTxt);
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
  private BufferedReader getDiffResults(Archive archive, String projectDir) {

    // check for existence of project Dir
    if (!Files.exists(Paths.get(projectDir))) {
      System.out.println("Path doesn't exist");
    }

    // fetch/update entire repo
    GitUtil.gitFetch("origin", projectDir);

    // if we ARE NOT doing a diff between two commits, then we are doing a diff
    // between two branches
    if(archive.getDiffBranch() == null) {

      // checkout and pull branch 2 first so that we don't have to re-checkout branch 1
      GitUtil.gitCheckoutPull(archive.getDiffParam2(), projectDir);
      GitUtil.gitCheckoutPull(archive.getDiffParam1(), projectDir);

    } else {
      GitUtil.gitCheckoutPull(archive.getDiffBranch(), projectDir);
    }

    InputStream is = GitUtil.gitDiff(archive.getDiffParam1(), archive.getDiffParam2(), projectDir).getInputStream();
    InputStreamReader isr = new InputStreamReader(is);

    return new BufferedReader(isr);
  }
}
