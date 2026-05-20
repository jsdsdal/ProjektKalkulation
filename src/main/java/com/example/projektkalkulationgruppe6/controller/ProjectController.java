package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.exception.ProjectNotFoundException;
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

    // TODO: fjern debug endpoint
    @GetMapping("/debug/{id}")
    @ResponseBody
    public String debug(@PathVariable int id) {
        Project project = projectService.findById(id);
        System.out.println(project);
        return "<pre>" + project.toString() + "</pre>";
    }

    @GetMapping("/table")
    public String list(Model model, HttpSession session) {
        sessionHelper.isLoggedIn(session);

        model.addAttribute("projects", projectService.findAll());

        return "projects/project-list";
    }

    @GetMapping("/create-project")
    public String showCreateForm(Model model, HttpSession session) {
        sessionHelper.isLoggedIn(session);

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

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);

        try {
            projectService.delete(id);
            return "redirect:projects/project-list";
        } catch (ProjectNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/404";
        } catch (InvalidProjectException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/400";
        }
    }



//    @PostMapping("/create-project")
//    public




}
