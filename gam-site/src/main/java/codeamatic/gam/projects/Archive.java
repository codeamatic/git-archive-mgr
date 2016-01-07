package codeamatic.gam.projects;

import org.hibernate.annotations.Type;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Model class for representing a single project
 */
@Entity
@Table(name = "archives")
public class Archive {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  private String appPrefix;

  @NotNull
  private String webPrefix;

  @NotNull
  private String deployDate;

  private String diffBranch;

  @NotNull
  private String diffParam1;

  @NotNull
  private String diffParam2;

  @NotNull
  @Type(type = "text")
  private String readmeTxt;

  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  public Archive() {
  }

  public Archive(String appPrefix, String webPrefix, String diffBranch, String diffParam1,
                 String diffParam2, String readmeTxt, String deployDate) {
    this.appPrefix = appPrefix;
    this.webPrefix = webPrefix;
    this.deployDate = deployDate;
    this.diffBranch = diffBranch;
    this.diffParam1 = diffParam1;
    this.diffParam2 = diffParam2;
    this.readmeTxt = readmeTxt;
  }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @PrePersist
  @PreUpdate
  protected void updateCreatedDate() {
    this.created = new Date();
  }
}
