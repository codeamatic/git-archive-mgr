package codeamatic.gam.projects.support;

import org.hibernate.validator.constraints.NotEmpty;

public class ProjectForm {

  @NotEmpty
  private String projectName;

  @NotEmpty
  private String projectOwner;

  @NotEmpty
  private String projectDirectory;

  @NotEmpty
  private String projectRepoUrl;

  @NotEmpty
  private String projectSiteUrl;

  @SuppressWarnings("unused")
  public ProjectForm() {}

  public ProjectForm(String projectName, String projectOwner, String projectDirectory, String projectRepoUrl, String projectSiteUrl) {
    this.projectName = projectName;
    this.projectOwner = projectOwner;
    this.projectDirectory = projectDirectory;
    this.projectRepoUrl = projectRepoUrl;
    this.projectSiteUrl = projectSiteUrl;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getProjectOwner() {
    return projectOwner;
  }

  public void setProjectOwner(String projectOwner) {
    this.projectOwner = projectOwner;
  }

  public String getProjectDirectory() {
    return projectDirectory;
  }

  public void setProjectDirectory(String projectDirectory) {
    this.projectDirectory = projectDirectory;
  }

  public String getProjectRepoUrl() {
    return projectRepoUrl;
  }

  public void setProjectRepoUrl(String projectRepoUrl) {
    this.projectRepoUrl = projectRepoUrl;
  }

  public String getProjectSiteUrl() {
    return projectSiteUrl;
  }

  public void setProjectSiteUrl(String projectSiteUrl) {
    this.projectSiteUrl = projectSiteUrl;
  }
}
