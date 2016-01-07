package codeamatic.gam.projects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Model class for representing a single project
 */
@Entity
@Table(name = "project")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  private String name;

  @NotNull
  private String owner;

  @NotNull
  private String directory;

  @NotNull
  private String testUrl;

  public Project(String name, String owner, String directory,
                 String testUrl) {
    this.name = name;
    this.owner = owner;
    this.directory = directory;
    this.testUrl = testUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getDirectory() {
    return directory;
  }

  public void setDirectory(String directory) {
    this.directory = directory;
  }

  public String getTestUrl() {
    return testUrl;
  }

  public void setTestUrl(String testUrl) {
    this.testUrl = testUrl;
  }
}
