package codeamatic.gam.projects.support;

import org.springframework.stereotype.Service;

import codeamatic.gam.projects.Archive;

@Service
public class ArchiveFormAdapater {

  public Archive createArchiveFromArchiveForm(ArchiveForm archiveForm) {
    Archive
        archive =
        new Archive(archiveForm.getProject(), archiveForm.getAppPrefix(),
                    archiveForm.getWebPrefix(), archiveForm.getDeployDate(),
                    archiveForm.getDiffBranch(), archiveForm.getDiffParam1(),
                    archiveForm.getDiffParam2());
    return archive;
  }
}
