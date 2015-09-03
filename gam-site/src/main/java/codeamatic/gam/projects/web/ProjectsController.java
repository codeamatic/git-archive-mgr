package codeamatic.gam.projects.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import codeamatic.gam.archives.Archive;
import codeamatic.gam.projects.Project;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Controller that handles requests for the projects overview page.
 */
@Controller
@RequestMapping("/projects")
public class ProjectsController {

  @RequestMapping(method = { GET, HEAD })
  public String listProjects(Model model) {
    return "projects/index";
  }

  @RequestMapping(value = "/add", method = { GET, HEAD })
  public String addProject(Model model) {
     return "";
  }

  @RequestMapping(value = "/{projectHash}", method = { GET, HEAD })
  public String showProject(@PathVariable String projectHash, Model model) {

    Project project = new Project();

    model.addAttribute("archive", new Archive());
    return "projects/details";
  }
}
