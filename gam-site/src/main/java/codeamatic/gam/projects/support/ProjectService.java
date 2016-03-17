package codeamatic.gam.projects.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import codeamatic.gam.projects.Project;
import codeamatic.gam.util.GitUtil;

/**
 * Service used for interacting with the project repository as well as for
 * handling functionality directly related to cloning projects from the
 * repository url.
 */
@Service
public class ProjectService {

  private static Logger logger = LogManager.getLogger(GitUtil.class);

  private final ProjectRepository projectRepository;

  private final ProjectFormAdapter projectFormAdapter;

  @Autowired
  public ProjectService(ProjectRepository projectRepository, ProjectFormAdapter projectFormAdapter) {
    this.projectRepository = projectRepository;
    this.projectFormAdapter = projectFormAdapter;
  }

  /**
   * Retrieve all projects from repository.
   *
   * @return list of projects, {@code null} otherwise
   */
  public List<Project> getProjects() {
    return projectRepository.findAll();
  }

  /**
   * Retrieve a single project with a project id matching what has been provided.
   *
   * @param id Integer project id
   * @return single project associated with provided id, {@code null} otherwise
   */
  public Project getProject(Integer id) {
    return projectRepository.findById(id);
  }

  /**
   * Adds project to repository and clones the associated Git repo to the project build directory
   *
   * @param projectForm ProjectForm model
   * @return a new Project object
   */
  public Project addProject(ProjectForm projectForm) throws IOException {
    Project project = projectFormAdapter.createProjectFromProjectForm(projectForm);

    // Create directory and clone project
    File buildDir = new File(project.getBuildDirectory());

    if (!buildDir.exists() && !buildDir.mkdirs()) {
      logger.error("Unable to create project directory");
      throw new IOException("Unable to create directory structure " + buildDir.getAbsolutePath());
    }

    cloneProject(project.getRepoUrl(), project.getBuildDirectory());

    // ONLY save the project if the cloning worked as expected.
    // TODO: throw exception if unable to clone
    //if(cloned) {
      projectRepository.save(project);
    //}

    return project;
  }

  /**
   * Clones the project from the Git repository into the projects build directory.
   *
   * @param repoUrl String the url of the projects repository
   * @param buildDir String the directory location where the project will be built
   * @return true if the project is cloned correctly, false otherwise
   */
  private List<String> cloneProject(String repoUrl, String buildDir) {
    // TODO: throw an uncloneable exception or something
    String gitClone = "git clone " + repoUrl + " .";

    // TODO: return something that will actually help us identify if the clone was successful.
    return GitUtil.processCommand(gitClone, buildDir);
  }
}
