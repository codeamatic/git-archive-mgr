package codeamatic.gam.archives.support;

import java.io.IOException;

import codeamatic.gam.archives.Archive;
import codeamatic.gam.projects.Project;

/**
 * Interface specifying a set of operations needed to process Archives.
 */
public interface ArchiveService {

  /**
   * Processes the archiving and comparison of deltas between git commits/branches.
   *
   * @param project populated project object
   * @param archive populated archive object
   * @return a string representation of the temporary directory where the zip archive is located
   */
  String process(Project project, Archive archive) throws IOException, InterruptedException;
}
