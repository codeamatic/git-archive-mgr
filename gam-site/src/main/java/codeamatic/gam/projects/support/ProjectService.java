package codeamatic.gam.projects.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import codeamatic.gam.projects.Project;
import codeamatic.gam.projects.util.GitUtil;

@Service
public class ProjectService {

  private final ProjectRepository projectRepository;

  private final ProjectFormAdapter projectFormAdapter;

  @Autowired
  public ProjectService(ProjectRepository projectRepository, ProjectFormAdapter projectFormAdapter) {
    this.projectRepository = projectRepository;
    this.projectFormAdapter = projectFormAdapter;
  }

  public List<Project> getProjects() {
    return projectRepository.findAll();
  }

  public Project getProject(Integer id) {
    return projectRepository.findById(id);
  }

  public Project getProjectName(String name) {
    return projectRepository.findByName(name);
  }

  public Project addProject(ProjectForm projectForm) {
    Project project = projectFormAdapter.createProjectFromProjectForm(projectForm);

    // Create directory and clone project
    File buildDir = new File(project.getBuildDirectory());

    if (!buildDir.exists() && !buildDir.mkdirs()) {
      System.out.println("Unable to create project directory");
    }

    cloneProject(project.getRepoUrl(), project.getBuildDirectory());

    projectRepository.save(project);
    return project;
  }

  private boolean cloneProject(String repoUrl, String buildDir) {
    String gitClone = "git clone " + repoUrl + " .";

    Process cloneProcess = GitUtil.processCommand(gitClone, buildDir);

    return (cloneProcess != null);
  }
}
