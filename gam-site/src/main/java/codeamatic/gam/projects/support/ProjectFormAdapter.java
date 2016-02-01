package codeamatic.gam.projects.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import codeamatic.gam.projects.Project;

@Service
public class ProjectFormAdapter {

  private final String baseBuildDirectory;

  @Autowired
  public ProjectFormAdapter(@Value("${gam.builddirectory.base}") String baseBuildDirectory) {
    this.baseBuildDirectory = baseBuildDirectory;
  }

  public Project createProjectFromProjectForm(ProjectForm projectForm) {
    // set project directory to be a hash of the project name
    String md5DigestAsHex = DigestUtils.md5DigestAsHex(projectForm.getProjectName().getBytes());
    String projectDirectory = baseBuildDirectory + "/" + md5DigestAsHex.substring(0, 10);

    Project
        project =
        new Project(projectForm.getProjectName(), projectForm.getProjectOwner(), projectDirectory,
                    projectForm.getProjectRepoUrl(), projectForm.getProjectSiteUrl());
    return project;
  }
}
