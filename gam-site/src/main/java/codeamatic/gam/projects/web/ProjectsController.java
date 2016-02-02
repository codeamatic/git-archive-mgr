package codeamatic.gam.projects.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import codeamatic.gam.projects.Archive;
import codeamatic.gam.projects.Project;
import codeamatic.gam.projects.support.ArchiveForm;
import codeamatic.gam.projects.support.ArchiveService;
import codeamatic.gam.projects.support.ProjectForm;
import codeamatic.gam.projects.support.ProjectService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller that handles requests for all project related pages.
 */
@Controller
@RequestMapping("/projects")
public class ProjectsController {

  private final ProjectService projectService;
  private final ArchiveService archiveService;

  @Autowired
  public ProjectsController(ProjectService projectService, ArchiveService archiveService) {
    this.projectService = projectService;
    this.archiveService = archiveService;
  }

  @RequestMapping(method = {GET, HEAD})
  public String listProjects(Model model) {
    model.addAttribute("projects", projectService.getProjects());
    return "projects/index";
  }

  @RequestMapping(value = "/add", method = {GET, HEAD})
  public String newProject(Model model) {
    model.addAttribute("projectForm", new ProjectForm());
    return "projects/edit";
  }

  @RequestMapping(value = "/add", method = {POST})
  public String addProject(@Valid ProjectForm projectForm, BindingResult bindingResult,
                           Model model) {

      if (bindingResult.hasErrors()) {
        return "projects/edit";
        // TODO: return list of errors
      } else {
        projectService.addProject(projectForm);
      }

    return "redirect:/projects";
  }

  @RequestMapping(value = "/{projectId:[0-9]+}", method = {GET, HEAD})
  public String showProject(@PathVariable Integer projectId, Model model) {

    ArchiveForm archiveForm = new ArchiveForm();
    archiveForm.setProject(projectService.getProject(projectId));

    try {
      Resource resource = new ClassPathResource("readme.template.txt");
      InputStream resourceInputStream = resource.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(resourceInputStream));
      String readmeTxt = "";

      while (br.ready()) {
        readmeTxt += br.readLine() + "\r\n";
      }
      br.close();

      archiveForm.setReadmeTxt(readmeTxt);
    } catch(IOException e) {
      // Template is only being loaded for convenience and ease of entry.
      //  If this fails....just pass through.
    }

    model.addAttribute("archiveForm", archiveForm);

    return "projects/details";
  }

  @ResponseBody
  @RequestMapping(value = "/{projectId:[0-9]+}/archives", method = {
      POST}, produces = "application/zip")
  public FileSystemResource processArchive(@PathVariable Integer projectId, ArchiveForm archiveForm,
                                           BindingResult bindingResult,
                                           HttpServletResponse httpServletResponse) {
    // TODO: Check archive object for errors, add BindingResult

    Project project = archiveForm.getProject();

    String zipPath = "";

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // replace README template holders {{tempHolder}}
    String readmeTxt = archiveForm.getReadmeTxt();
    readmeTxt = readmeTxt.replace("{{ProjectName}}", project.getName());
    readmeTxt = readmeTxt.replace("{{ArchiveDeployDate}}", archiveForm.getDeployDate());
    readmeTxt = readmeTxt.replace("{{ProjectTestUrl}}", project.getSiteUrl());
    readmeTxt = readmeTxt.replace("{{CurrentDate}}", simpleDateFormat.format(date));

    archiveForm.setReadmeTxt(readmeTxt);

    try {
      Archive archive = archiveService.addArchive(archiveForm);
      zipPath = archiveService.process(archive);
      // save archive to db
     // archiveRepository.save(archive);
    } catch (IOException | InterruptedException e) {
      // TODO: Add better error handling
      System.out.println(e.getMessage());
    }

    String zipName = "From_" + project.getOwner() + "_" + project.getName() + ".zip";

    httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
    return new FileSystemResource(zipPath);
  }
}
