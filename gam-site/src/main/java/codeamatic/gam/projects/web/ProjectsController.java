package codeamatic.gam.projects.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import codeamatic.gam.archives.Archive;
import codeamatic.gam.archives.support.ArchiveProcessor;
import codeamatic.gam.projects.Project;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller that handles requests for the projects overview page.
 */
@Controller
@RequestMapping("/projects")
public class ProjectsController {

  private final ArchiveProcessor archiveProcessor;

  @Autowired
  public ProjectsController(ArchiveProcessor archiveProcessor) {
    this.archiveProcessor = archiveProcessor;
  }

  @RequestMapping(method = {GET, HEAD})
  public String listProjects(Model model) {
    return "projects/index";
  }

  @RequestMapping(value = "/add", method = {GET, HEAD})
  public String addProject(Model model) {
    return "";
  }

  @RequestMapping(value = "/{projectHash}", method = {GET, HEAD})
  public String showProject(@PathVariable String projectHash, Model model) {

    Project project = new Project();
    project.setProjectDirectory(
        "C:\\Users\\jason.webb\\Dev\\_projects\\ConAgra\\rse_rockfish\\conagra-foods");
    project.setProjectName("ReadySetEat");
    project.setProjectOwner("Rockfish");

    model.addAttribute("archive", new Archive());
    return "projects/details";
  }

  @ResponseBody
  @RequestMapping(value = "/{projectHash}", method = {POST}, produces = "application/zip")
  public FileSystemResource processArchive(@PathVariable String projectHash, Archive archive, HttpServletResponse httpServletResponse) {
    // TODO: Check archive object for errors, add BindingResult

    Project project = new Project();
    project.setProjectDirectory(
        "C:\\Users\\jason.webb\\Dev\\_projects\\ConAgra\\rse_rockfish\\conagra-foods");
    project.setProjectName("ReadySetEat");
    project.setProjectOwner("Rockfish");

    String zipPath = "";

    try {
      zipPath = archiveProcessor.process(project, archive);
    } catch (IOException | InterruptedException e) {
      // TODO: Add better error handling
      System.out.println(e.getMessage());
    }

    String zipName = "From_" + project.getProjectOwner() + "_" + project.getProjectName() + ".zip";

    httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
    return new FileSystemResource(zipPath);
  }
}
