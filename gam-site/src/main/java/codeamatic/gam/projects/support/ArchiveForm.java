package codeamatic.gam.projects.support;

import org.hibernate.validator.constraints.NotEmpty;

import codeamatic.gam.projects.Project;

public class ArchiveForm {

  @NotEmpty
  private Project project;

  @NotEmpty
  private String appPrefix;

  @NotEmpty
  private String webPrefix;

  @NotEmpty
  private String deployDate;

  private String diffBranch;

  @NotEmpty
  private String diffParam1;

  @NotEmpty
  private String diffParam2;

  @NotEmpty
  private String readmeTxt;

  @SuppressWarnings("unused")
  public ArchiveForm() {
  }

  public ArchiveForm(Project project, String appPrefix, String webPrefix, String deployDate,
                     String diffBranch, String diffParam1, String diffParam2, String readmeTxt) {
    this.project = project;
    this.appPrefix = appPrefix;
    this.webPrefix = webPrefix;
    this.deployDate = deployDate;
    this.diffBranch = diffBranch;
    this.diffParam1 = diffParam1;
    this.diffParam2 = diffParam2;
    this.readmeTxt = readmeTxt;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public String getAppPrefix() {
    return appPrefix;
  }

  public void setAppPrefix(String appPrefix) {
    this.appPrefix = appPrefix;
  }

  public String getWebPrefix() {
    return webPrefix;
  }

  public void setWebPrefix(String webPrefix) {
    this.webPrefix = webPrefix;
  }

  public String getDeployDate() {
    return deployDate;
  }

  public void setDeployDate(String deployDate) {
    this.deployDate = deployDate;
  }

  public String getDiffBranch() {
    return diffBranch;
  }

  public void setDiffBranch(String diffBranch) {
    this.diffBranch = diffBranch;
  }

  public String getDiffParam1() {
    return diffParam1;
  }

  public void setDiffParam1(String diffParam1) {
    this.diffParam1 = diffParam1;
  }

  public String getDiffParam2() {
    return diffParam2;
  }

  public void setDiffParam2(String diffParam2) {
    this.diffParam2 = diffParam2;
  }

  public String getReadmeTxt() {
    return readmeTxt;
  }

  public void setReadmeTxt(String readmeTxt) {
    this.readmeTxt = readmeTxt;
  }
}
