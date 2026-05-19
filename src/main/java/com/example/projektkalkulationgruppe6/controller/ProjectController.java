package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final SessionHelper sessionHelper;

    public ProjectController(ProjectService projectService, EmployeeService employeeService, SessionHelper sessionHelper) {
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping()
    public String home(HttpSession session) {
        sessionHelper.isLoggedIn(session);
        return "home";
    }

    @GetMapping("/table")
    public String list(Model model) {
        return "projects/project-list";
    }

    @GetMapping("/create-project")
    public String showCreateForm(Model model, HttpSession session) {
        sessionHelper.isLoggedIn(session);
        //tjek for session login
        model.addAttribute("project", new Project());
        model.addAttribute("formTitle", "Create Project");
        model.addAttribute("statusList", projectService.findAllStatusList());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("formAction", "/projects/create-project");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/projects/table");
        return "projects/project-form";
    }

    @PostMapping("/create-project")
    public String create(@ModelAttribute Project project, @RequestParam int projectOwnerId, Model model, HttpSession session) {
        sessionHelper.isLoggedIn(session);
        //tjek for session login
        try {
            Employee projectOwner = employeeService.findById(projectOwnerId);
            project.setProjectOwner(projectOwner);
            projectService.create(project);
            return "redirect:/projects/project-list";
        } catch (InvalidProjectException ex) {
            model.addAttribute("project", project);
            model.addAttribute("formTitle", "Create Project");
            model.addAttribute("statusList", projectService.findAllStatusList());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("formAction", "/projects/create-project");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("cancelUrl", "/projects/table");
            return "projects/project-form";
        }
    }


//    @PostMapping("/create-project")
//    public




}
