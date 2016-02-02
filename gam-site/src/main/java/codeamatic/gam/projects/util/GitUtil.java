package codeamatic.gam.projects.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple util class used to provide convenience methods that simplify Git commands processing
 */
public class GitUtil {

  /**
   * Takes a string command, tokenizes it into smaller strings to be added to the process builder
   * for execution.
   *
   * @param command    String command to be tokenized and processed/executed
   * @param projectDir String directory where commands should be ran
   * @return Process object
   */
  public static Process processCommand(String command, String projectDir) {
    Process process = null;
    List<String> processList = new ArrayList<>();
    String[] tokens = command.split(" ");

    // Break commands into individual tokens and add to process
    // list for building
    processList.addAll(Arrays.asList(tokens));

    if (!processList.isEmpty()) {
      try {
        ProcessBuilder pb = new ProcessBuilder(processList);
        pb.directory(new File(projectDir));

        process = pb.start();
        process.waitFor();

      } catch (IOException | InterruptedException ex) {
        // TODO: don't fall through
      }
    }

    return process;
  }
}
