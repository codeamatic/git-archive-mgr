package codeamatic.gam.projects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Model class for representing a single project
 */
@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String owner;

  @Column(nullable = false)
  private String repoUrl;

  @Column(nullable = false)
  private String siteUrl;

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private boolean active = true;

  @Column(nullable = false, updatable = false, columnDefinition = "INT(11)")
  private Long created = new Date().getTime() / 1000;

  @SuppressWarnings("unused")
  public Project() {
  }

  public Project(String name, String owner, String repoUrl,
                 String siteUrl) {
    this.name = name;
    this.owner = owner;
    this.repoUrl = repoUrl;
    this.siteUrl = siteUrl;
  }

  public Integer getId() {
    return id;
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

  public String getRepoUrl() {
    return repoUrl;
  }

  public void setRepoUrl(String repoUrl) {
    this.repoUrl = repoUrl;
  }

  public String getSiteUrl() {
    return siteUrl;
  }

  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Long getCreated() {
    return created;
  }
}
