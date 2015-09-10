package codeamatic.gam.projects;

import java.util.List;

import codeamatic.gam.archives.Archive;

/**
 * Model class for representing a single project
 */
public class Project {

  private String projectName;

  private String projectOwner;

  private List<Archive> archiveList;

  private String projectDirectory;

  private String projectTestUrl;

  public Project() {
  }

  public Project(String projectName, String projectOwner,
                 List<Archive> archiveList, String projectDirectory, String projectTestUrl) {
    this.projectName = projectName;
    this.projectOwner = projectOwner;
    this.archiveList = archiveList;
    this.projectDirectory = projectDirectory;
    this.projectTestUrl = projectTestUrl;
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

  public List<Archive> getArchiveList() {
    return archiveList;
  }

  public void setArchiveList(List<Archive> archiveList) {
    this.archiveList = archiveList;
  }

  public String getProjectDirectory() {
    return projectDirectory;
  }

  public void setProjectDirectory(String projectDirectory) {
    this.projectDirectory = projectDirectory;
  }

  public String getProjectTestUrl() {
    return projectTestUrl;
  }

  public void setProjectTestUrl(String projectTestUrl) {
    this.projectTestUrl = projectTestUrl;
  }
}
