package codeamatic.gam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Properties;

import codeamatic.gam.archive.support.CopyFileVisitor;

/**
 * The entry point for the Gam application.
 */
public class Application {

  /**
   * Starting point for application execution
   */
  public static void main(String[] args) throws Exception {

    Application app = new Application();
    Properties prop = app.retrieveGamProperties();

    BufferedReader
        br =
        app.getDiffResults(prop.getProperty("gam.repoDir"), prop.getProperty("gam.diffBranch"),
                           prop.getProperty("gam.diffParam1"),
                           prop.getProperty("gam.diffParam2"));
    String line;

    // Retrieve branch diff
    List<String> appList = new ArrayList<>();
    List<String> webList = new ArrayList<>();

    while ((line = br.readLine()) != null) {
      if (line.startsWith(prop.getProperty("gam.webPrefix"))) {
        webList.add(line);
      } else if (line.startsWith(prop.getProperty("gam.appPrefix"))) {
        appList.add(line);
      } else {
        // do nothing for now....
      }
    }

    app.generateZips(webList, appList, prop);
  }

  /**
   * Process and generate zip archives for
   *
   * @param appList
   * @param webList
   * @param properties
   * @throws IOException
   */
  private void generateZips(List<String> appList, List<String> webList, Properties properties)
      throws IOException {

    // Do nothing if the files haven't changed
    if(appList.isEmpty() && webList.isEmpty()) {
      System.out.println("No files have changed.");
      return;
    }

    String fromAuthor = "From_" + properties.getProperty("gam.projectAuthor");

    // Create temp directory used for building archive
    Path
        tempDir =
        Files.createTempDirectory(properties.getProperty("gam.projectName").toLowerCase());
    Path prjStructureOut;

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    if (Files.exists(tempDir)) {
      prjStructureOut =
          Paths.get(
              tempDir + "/" + fromAuthor + "/" + properties.getProperty("gam.projectName")
              + "/" + simpleDateFormat.format(date));
      Files.createDirectories(prjStructureOut);
    } else {
      // kill it...
      System.out.println("ERROR creating temporary directory.");
      return;
    }

    final Map<String, String> env = new HashMap<>();
    env.put("create", "true");

    if (!webList.isEmpty()) {
      URI
          uri =
          URI.create("jar:file:/" + prjStructureOut.toString().replace("\\", "/") + "/web.zip");

      System.out.println("# Web Updates");

      try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

        for (String file : webList) {
          Path source = Paths.get(properties.getProperty("gam.repoDir") + "/" + file);

          if (Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          System.out.println(file);

          Path target = zipfs.getPath(file.replace(properties.getProperty("gam.webPrefix"), ""));
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
          Path source = Paths.get(properties.getProperty("gam.repoDir") + "/" + file);

          if (Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          System.out.println(file);

          Path target = zipfs.getPath(file.replace(properties.getProperty("gam.appPrefix"), ""));
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
                   + properties.getProperty("gam.projectName") + ".zip");
    try (FileSystem zipFinal = FileSystems.newFileSystem(uri, env)) {
      Path source = Paths.get(tempDir + "/" + fromAuthor);
      Path target = zipFinal.getPath(fromAuthor);

      Files.walkFileTree(source, new CopyFileVisitor(target));
    }

    System.out.println("Complete with diffing");
  }

  /**
   * Retrieves the property values necessary to process and execute the git command line requests as
   * well as the archiving of the resulting fileset.
   *
   * @return Properties directly related
   */
  private Properties retrieveGamProperties() {
    Properties gamProp = new Properties();
    InputStream input = null;

    try {
      input = new FileInputStream("gam.properties");

      // load properties file
      gamProp.load(input);

    } catch (IOException ex) {
      System.out.println("ERROR retrieving Gam properties - " + ex.getMessage());
    } finally {
      // Attempt to close the input stream
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          System.out.println("ERROR closing input stream - " + e.getMessage());
        }
      }
    }

    return gamProp;
  }

  /**
   * @param startingDir Directory that pre-processing and result processing should start in
   * @param branch      Branch of the repository to be used for diffing commits
   * @param diffParam1  A branch to be used when diffing branches or a commit hex when diffing
   *                    commits
   * @param diffParam2  A branch to be used when diffing branches or a commit hex when diffing
   *                    commits
   * @return a BufferedReader object ready to process the resulting lines
   */
  private BufferedReader getDiffResults(String startingDir, String branch, String diffParam1,
                                        String diffParam2)
      throws IOException, InterruptedException {

    // Fetch origin on whatever branch is being used.
    List<String> processFetchList = new ArrayList<>();
    processFetchList.add("git");
    processFetchList.add("fetch");
    processFetchList.add("origin");

    // Is this a commit based diff?
    if (branch != null) {
      processFetchList.add(branch);
    }

    // Build process for fetch
    ProcessBuilder pbFetch = new ProcessBuilder(processFetchList);
    pbFetch.directory(new File(startingDir));
    // Create fetch origin process
    Process processFetch = pbFetch.start();

    // wait until fetching has been completed
    processFetch.waitFor();

    List<String> processList = new ArrayList<>();
    processList.add("git");
    processList.add("diff");
    processList.add("--name-only");
    processList.add(diffParam1);
    processList.add(diffParam2);
    ProcessBuilder processBuilder = new ProcessBuilder(processList);
    processBuilder.directory(new File(startingDir));

    Process process = processBuilder.start();
    InputStream is = process.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);

    return new BufferedReader(isr);
  }
}