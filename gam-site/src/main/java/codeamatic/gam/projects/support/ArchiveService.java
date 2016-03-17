package codeamatic.gam.projects.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
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

  private static Logger logger = LogManager.getLogger(GitUtil.class);

  private final ArchiveFormAdapater archiveFormAdapater;

  private String readmeTxt;

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
    List<String> diffOutput = getDiffResults(archive, project.getBuildDirectory());

    // Retrieve branch diff
    List<String> appList = new ArrayList<>();
    List<String> webList = new ArrayList<>();
    List<String> otherList = new ArrayList<>();

    String webPrefix = archive.getWebPrefix();
    String appPrefix = archive.getAppPrefix();

    for (String line : diffOutput) {
      if (line.startsWith(webPrefix)) {
        webList.add(line);
      } else if (line.startsWith(appPrefix)) {
        appList.add(line);
      } else {
        // add to a separate list used for various files that weren't
        // part of the app or web list.  This could be .xml files, config files, source code, etc.
        // purpose of this list is more for logging/outputting additional information
        otherList.add(line);
      }
    }

    return generateZips(appList, webList, archive);
  }

  /**
   * Process and generate zip archive, returning a path where the zip file exists
   *
   * @param appList List of strings that identify modified application related files
   * @param webList List of strings that identify modified web/static related files
   * @param archive Archive object representing a new archive
   * @return a generated zip path, or null
   * @throws IOException
   */
  private String generateZips(List<String> appList, List<String> webList, Archive archive)
      throws IOException {

    // Do nothing if the files haven't changed
    if (appList.isEmpty() && webList.isEmpty()) {
      logger.info("There is no difference between {} and  {}.", archive.getDiffParam1(), archive.getDiffParam2());
      return null;
    }

    Project project = archive.getProject();
    String fromAuthor = "From_" + project.getOwner();

    // Create temp directory used for building archive
    Path tempDir = Files.createTempDirectory(project.getName().toLowerCase());

    if(! Files.exists(tempDir)) {
      logger.error("Error creating a temporary directory.");
      // TODO: throw a custom exception
      return null;
    }

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    // Create approved directory structure From_X => Project Name => Date => etc
    Path prjStructureOut =
          Paths.get(
              tempDir + "/" + fromAuthor + "/" + project.getName()
              + "/" + simpleDateFormat.format(date));
    Files.createDirectories(prjStructureOut);

    final Map<String, String> env = new HashMap<>();
    env.put("create", "true");

    readmeTxt = archive.getReadmeTxt();
    String projectStr = prjStructureOut.toString();

    // Build WebList
    if(! webList.isEmpty()) {
      buildFileList(webList, archive, env, projectStr, "web");
    }

    // Build AppList
    if(! appList.isEmpty()) {
      buildFileList(appList, archive, env, projectStr, "app");
    }

    // Write readme data to README txt file
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(prjStructureOut.toString().replace("\\", "/") + "/README.txt"),
        "utf-8"))) {
      writer.write(readmeTxt);
    }

    // Create final zip
    URI uri = URI.create("jar:file:/" + tempDir.toString().replace("\\", "/") + "/" + fromAuthor + "_" + project.getName() + ".zip");

    logger.info(readmeTxt);

    return generateZipPath(env, uri, tempDir.toString(), fromAuthor);
  }

  /**
   *
   * @param fileList List of string identifying web or application files that have been modified
   * @param archive Archive object representing the current archive
   * @param env Map listing of properties to be passed for file creation
   * @param projectStructure String representing temporary directory structure to be used in the zip packaging
   * @param zipFilename The name of the zip file to be created
   */
  private void buildFileList(List<String> fileList, Archive archive, Map<String, String> env, String projectStructure, String zipFilename) {
    Project project = archive.getProject();

    // remove leading forward slash if applicable
    if(projectStructure.charAt(0) != '/') {
      projectStructure = "/" + projectStructure;
    }

    URI uri = URI.create("jar:file:" + projectStructure.replace("\\", "/") + "/" + zipFilename + ".zip");

    readmeTxt += "\r\n" + "# Web Updates" + "\r\n";

    try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

      for (String file : fileList) {
        Path source = Paths.get(project.getBuildDirectory() + "/" + file);

        if (Files.notExists(source)) {
          // File was deleted, log it to README
          logger.info("File [{}] was deleted from branch/commit.", source.toString());
          continue;
        }

        // replace the prefixes
        file = file.replace(archive.getWebPrefix(), "");
        file = file.replace(archive.getAppPrefix(), "");

        readmeTxt += file + "\r\n";

        Path target = zipfs.getPath(file);
        Path parentTargetDir = target.getParent();

        if (parentTargetDir != null && !Files.exists(parentTargetDir)) {
          Files.createDirectories(parentTargetDir);
        }

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
      }
    }  catch(IOException ex) {
      logger.error("Unable to create web.zip - {}", ex.getMessage());
    }
  }

  /**
   * Generate zip path to be used for creating a zip file
   *
   * @param env Map listing of properties to be passed for file creation
   * @param uri URI path for the zip file structure
   * @param sourceDir String of temporary directory location
   * @param projectAuthor String representing the author of the project
   * @return String zip path
   */
  private String generateZipPath(Map<String, String> env, URI uri, String sourceDir, String projectAuthor) {
    String zipPath = "";

    try (FileSystem zipFinal = FileSystems.newFileSystem(uri, env)) {
      Path source = Paths.get(sourceDir + "/" + projectAuthor);
      Path target = zipFinal.getPath(projectAuthor);

      Files.walkFileTree(source, new CopyFileVisitor(target));
      zipPath = zipFinal.toString();
    } catch(IOException ex) {
       logger.error("Unable to create new File System for zip - {}", ex.getMessage());
    }

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
  private List<String> getDiffResults(Archive archive, String projectDir) {

    // check for existence of project Dir
    if (!Files.exists(Paths.get(projectDir))) {
      logger.error("The project directory [{}] doesn't exist.", projectDir);
    }

    // fetch/update entire repo
    GitUtil.gitFetch("origin", projectDir);

    // if we ARE NOT doing a diff between two commits, then we are doing a diff
    // between two branches
    if (archive.getDiffBranch().isEmpty()) {

      // checkout and pull branch 2 first so that we don't have to re-checkout branch 1
      GitUtil.gitCheckoutPull(archive.getDiffParam2(), projectDir);
      GitUtil.gitCheckoutPull(archive.getDiffParam1(), projectDir);

    } else {
      GitUtil.gitCheckoutPull(archive.getDiffBranch(), projectDir);
    }

    return GitUtil.gitDiff(archive.getDiffParam1(), archive.getDiffParam2(), projectDir);
  }
}
