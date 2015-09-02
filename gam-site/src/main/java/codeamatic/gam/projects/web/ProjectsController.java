package codeamatic.gam.projects.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
