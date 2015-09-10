package codeamatic.gam.archives;

public class Archive {

  private String appPrefix;

  private String webPrefix;

  private String diffBranch;

  private String diffParam1;

  private String diffParam2;

  private String readmeTxt;

  public Archive() {
  }

  public Archive(String appPrefix, String webPrefix, String diffBranch, String diffParam1,
                 String diffParam2, String readmeTxt) {
    this.appPrefix = appPrefix;
    this.webPrefix = webPrefix;
    this.diffBranch = diffBranch;
    this.diffParam1 = diffParam1;
    this.diffParam2 = diffParam2;
    this.readmeTxt = readmeTxt;
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
