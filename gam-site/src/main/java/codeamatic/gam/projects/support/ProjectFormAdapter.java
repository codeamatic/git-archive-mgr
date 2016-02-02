package codeamatic.gam.projects.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import codeamatic.gam.projects.Project;

/**
 * Service used for mapping a {@link ProjectForm} to a {@link Project}.
 */
@Service
public class ProjectFormAdapter {

  private final String baseBuildDirectory;

  @Autowired
  public ProjectFormAdapter(@Value("${gam.builddirectory.base}") String baseBuildDirectory) {
    this.baseBuildDirectory = baseBuildDirectory;
  }

  /**
   * Creates a new {@link Project} from the user submitted {@link ProjectForm}.  Generates a build
   * directory by using the {@link #baseBuildDirectory} + a substring of a md5 hash created using
   * the project name.
   *
   * @param projectForm populated projectForm object
   * @return a mapped project
   */
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
