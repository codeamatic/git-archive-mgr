package codeamatic.gam.projects.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import codeamatic.gam.projects.Archive;
import codeamatic.gam.projects.support.ArchiveRepository;
import codeamatic.gam.projects.support.ArchiveService;
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

  private final ArchiveService archiveService;
  private final ArchiveRepository archiveRepository;

  @Autowired
  public ProjectsController(ArchiveService archiveService, ArchiveRepository archiveRepository) {
    this.archiveService = archiveService;
    this.archiveRepository = archiveRepository;
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

//    Project project = new Project();
//    project.setProjectDirectory(
//        "E:\\Dev\\_projects\\Rockfish\\ConAgra\\rse_rockfish");
//    project.setProjectName("ReadySetEat");
//    project.setProjectOwner("Rockfish");
    Archive archive = new Archive();

    try {
      Resource resource = new ClassPathResource("readme.template.txt");
      InputStream resourceInputStream = resource.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(resourceInputStream));
      String readmeTxt = "";

      while (br.ready()) {
        readmeTxt += br.readLine() + "\r\n";
      }
      br.close();

      archive.setReadmeTxt(readmeTxt);
    } catch(IOException e) {
      // Template is only being loaded for convenience and ease of entry.
      //  If this fails....just pass through.
    }

    model.addAttribute("archive", archive);
    return "projects/details";
  }

  @ResponseBody
  @RequestMapping(value = "/{projectHash}", method = {POST}, produces = "application/zip")
  public FileSystemResource processArchive(@PathVariable String projectHash, Archive archive, HttpServletResponse httpServletResponse) {
    // TODO: Check archive object for errors, add BindingResult

//    Project project = new Project();
//    project.setProjectDirectory(
//        "E:\\Dev\\_projects\\Rockfish\\ConAgra\\rse_rockfish");
//    project.setProjectName("ReadySetEat");
//    project.setProjectOwner("Rockfish");
//    project.setProjectTestUrl("http://www.rse2.staging.cinjweb.rfisite.com");

    String zipPath = "";

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // replace README template holders {{tempHolder}}
//    String readmeTxt = archive.getReadmeTxt();
//    readmeTxt = readmeTxt.replace("{{ProjectName}}", project.getProjectName());
//    readmeTxt = readmeTxt.replace("{{ArchiveDeployDate}}", archive.getDeployDate());
//    readmeTxt = readmeTxt.replace("{{ProjectTestUrl}}", project.getProjectTestUrl());
//    readmeTxt = readmeTxt.replace("{{CurrentDate}}", simpleDateFormat.format(date));
//
//  archive.setReadmeTxt(readmeTxt);

//    try {
//      //zipPath = archiveService.process(project, archive);
//      // save archive to db
//     // archiveRepository.save(archive);
//    } catch (IOException | InterruptedException e) {
//      // TODO: Add better error handling
//      System.out.println(e.getMessage());
//    }

    //String zipName = "From_" + project.getProjectOwner() + "_" + project.getProjectName() + ".zip";

    //httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
    return null; //new FileSystemResource(zipPath);
  }
}
