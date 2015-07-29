package codeamatic.gam;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codeamatic.gam.archive.support.CopyFileVisitor;

/**
 * The entry point for the Gam application.
 */
public class Application {

  public static void main(String[] args) throws Exception {

    // check for arguments as well as a configuration file
    if(args.length > 0) {

    }

    String projectAuthor = "Rockfish";
    String projectName = "KidCuisine";
    String webPrefix = "apache-static/";
    String appPrefix = "tomcat-dynamic/";
    String diffParam1= "29eff57c141";
    String diffParam2 = "4b57af2a77a";
    String repoDir = "E:\\Dev\\_projects\\Rockfish\\ConAgra\\kidcuisine";

    String fromAuthor = "From_" + projectAuthor;

    List<String> processList = new ArrayList<>();
    processList.add("git");
    processList.add("diff");
    processList.add("--name-only");
    processList.add(diffParam1);
    processList.add(diffParam2);
    ProcessBuilder processBuilder = new ProcessBuilder(processList);
    processBuilder.directory(new File(repoDir));

    Process process = processBuilder.start();
    InputStream is = process.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);
    String line;
    // Retrieve branch diff
    List<String> appList = new ArrayList<>();
    List<String> webList = new ArrayList<>();

    while ((line = br.readLine()) != null) {
      if (line.startsWith(webPrefix)) {
        webList.add(line);
      } else if (line.startsWith(appPrefix)) {
        appList.add(line);
      } else {
        // do nothing for now....
      }
    }

    // Create temp directory used for building archive
    Path tempDir = Files.createTempDirectory(projectName.toLowerCase());
    Path prjStructureOut = null;

    if (Files.exists(tempDir)) {
      prjStructureOut = Paths.get(tempDir + "/" + fromAuthor + "/" + projectName + "/20170725");
      Files.createDirectories(prjStructureOut);
    } else {
      // kill the process...
    }

    final Map<String, String> env = new HashMap<>();
    env.put("create", "true");

    if (!webList.isEmpty()) {
      URI
          uri =
          URI.create("jar:file:/" + prjStructureOut.toString().replace("\\", "/") + "/web.zip");

      try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

        for (String file : webList) {
          Path source = Paths.get(repoDir + "/" + file);

          if(Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          Path target = zipfs.getPath(file.replace(webPrefix, ""));
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

      try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

        for (String file : appList) {
          Path source = Paths.get(repoDir + "/" + file);

          if(Files.notExists(source)) {
            // File was deleted, log it to README
            System.out.println("File was removed " + source.toString());
            continue;
          }

          Path target = zipfs.getPath(file.replace(appPrefix, ""));
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
        URI.create("jar:file:/" + tempDir.toString().replace("\\", "/") + "/" + fromAuthor + "_" + projectName + " .zip");
    try (FileSystem zipFinal = FileSystems.newFileSystem(uri, env)) {
      Path source = Paths.get(tempDir + "/" + fromAuthor);
      Path target = zipFinal.getPath(fromAuthor);

      Files.walkFileTree(source, new CopyFileVisitor(target));
    }

    System.out.println("Complete with diffing");
  }
}