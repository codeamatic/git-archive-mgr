package codeamatic.gam.projects.support;

import org.springframework.stereotype.Service;

import codeamatic.gam.projects.Archive;

/**
 * Service used for mapping a {@link ArchiveForm} to an {@link Archive}.
 */
@Service
public class ArchiveFormAdapater {

  /**
   * Creates a new {@link Archive} from the user submitted {@link ArchiveForm}.
   *
   * @param archiveForm populated archiveForm object
   * @return a mapped archive
   */
  public Archive createArchiveFromArchiveForm(ArchiveForm archiveForm) {
    Archive
        archive =
        new Archive(archiveForm.getProject(), archiveForm.getAppPrefix(),
                    archiveForm.getWebPrefix(), archiveForm.getDeployDate(),
                    archiveForm.getDiffBranch(), archiveForm.getDiffParam1(),
                    archiveForm.getDiffParam2(), archiveForm.getReadmeTxt());
    return archive;
  }
}
