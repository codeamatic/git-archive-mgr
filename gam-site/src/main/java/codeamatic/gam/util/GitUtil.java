package codeamatic.gam.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple util class used to provide convenience methods that simplify Git commands processing
 */
public class GitUtil {

  private static Logger logger = LogManager.getLogger(GitUtil.class);

  /**
   * Execute a diff between two branches, tags or commit points
   *
   * @param param1 String first parameter (branch or commit hex)
   * @param param2 String second parameter (branch or commit hex)
   * @param projectDir String directory to be built in
   */
  public static List<String> gitDiff(String param1, String param2, String projectDir) {
    String gitDiff = "git diff --name-only %s %s";

    String diffCmd = String.format(gitDiff, param1, param2);

    return processCommand(diffCmd, projectDir);
  }

  /**
   * Fetch the provided remote name
   *
   * @param remote String remote name
   * @param projectDir String directory to be built in
   */
  public static List<String> gitFetch(String remote, String projectDir) {
    String gitFetch = "git fetch %s";

    String fetchCmd = String.format(gitFetch, remote);

    return processCommand(fetchCmd, projectDir);
  }

  /**
   * Checkout and pull (rebase) branch while working from the project directory.
   *
   * @param branch String branch to be checked out and updated
   * @param projectDir String directory to be built in
   */
  public static List<String> gitCheckoutPull(String branch, String projectDir) {
    String gitCheckout = "git checkout %s";
    String gitPull = "git pull --rebase origin %s";

    String checkoutCmd = String.format(gitCheckout, branch);
    String pullCmd = String.format(gitPull, branch);

    processCommand(checkoutCmd, projectDir);
    return processCommand(pullCmd, projectDir);
  }

  /**
   * Takes a string command, tokenizes it into smaller strings to be added to the process builder
   * for execution.
   *
   * @param command    String command to be tokenized and processed/executed
   * @param projectDir String directory where commands should be ran
   * @return BufferedReader object
   */
  public static List<String> processCommand(String command, String projectDir) {
    List<String> processOutput = new ArrayList<>();
    BufferedReader br = null;
    List<String> processList = new ArrayList<>();
    String[] tokens = command.split(" ");

    logger.debug("Process Git command - {}", command);

    // Break commands into individual tokens and add to process
    // list for building
    processList.addAll(Arrays.asList(tokens));

    if (!processList.isEmpty()) {
      try {
        String line;
        ProcessBuilder pb = new ProcessBuilder(processList);
        pb.directory(new File(projectDir));
        Process process = pb.start();

        br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Avoids a deadlock from the buffer filling up
        while ((line = br.readLine()) != null) {
          processOutput.add(line);
        }

        process.waitFor();
      } catch (IOException | InterruptedException ex) {
        logger.error("Unable to process command - {}.  Exception - {}", command, ex.getMessage());
      }
    }

    return processOutput;
  }
}
