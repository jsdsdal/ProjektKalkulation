package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/create-project")
    public String showCreateForm(Model model) {
        //tjek for session login
        model.addAttribute("project", new Project());
        model.addAttribute("formTitle", "Create Project");
        model.addAttribute("statusList", projectService.findAllStatusList());
        model.addAttribute("formAction", "/home");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/home");
        return "projects/project-form";
    }

    @PostMapping()
    public String create(@ModelAttribute Project project, Model model) {
        //tjek for session login
        try {
            projectService.create(project);
            return "redirect:/home";
        } catch (InvalidProjectException ex) {
            model.addAttribute("project", project);
            model.addAttribute("formTitle", "Create Project");
            model.addAttribute("formAction", "/home");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("cancelUrl", "/home");
            return "projects/project-form";
        }
    }


//    @PostMapping("/create-project")
//    public




}
