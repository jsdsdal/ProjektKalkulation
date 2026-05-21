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

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);

        Project project = projectService.findById(id);

        model.addAttribute("project", project);
        model.addAttribute("formTitle", "Edit Project");
        model.addAttribute("formAction", "/projects/" + id + "/edit");
        model.addAttribute("submitLabel", "Save");
        model.addAttribute("cancelUrl", "/projects/project-list");
        model.addAttribute("statusList", projectService.findAllStatusList());
        model.addAttribute("employees", employeeService.findAll());

        return "projects/project-form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable int id,
                       @ModelAttribute Project project,
                       HttpSession session,
                       Model model) {
        sessionHelper.isLoggedIn(session);

        try {
            int employeeId = (int) session.getAttribute("employeeId");
            project.setProjectOwner(employeeService.findById(employeeId));
            projectService.update(id, project);
            return "redirect:/projects/project-list";
        } catch (ProjectNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/404";
        } catch (InvalidProjectException ex) {
            model.addAttribute("project", project);
            model.addAttribute("formTitle", "Edit Project");
            model.addAttribute("formAction", "/projects/" + id + "/edit");
            model.addAttribute("submitLabel", "Save");
            model.addAttribute("cancelUrl", "/projects/project-list");
            model.addAttribute("statusList", projectService.findAllStatusList());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            return "projects/project-form";
        }
    }



//    @PostMapping("/create-project")
//    public




}
