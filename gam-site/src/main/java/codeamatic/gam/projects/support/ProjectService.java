package codeamatic.gam.projects.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import codeamatic.gam.projects.Project;

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
    projectRepository.save(project);
    return project;
  }

  private boolean cloneProject(String repoUrl) {
    return false;
  }
}
