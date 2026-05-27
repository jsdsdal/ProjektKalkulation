package com.example.projektkalkulationgruppe6.controller;



import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class DashboardController {

    private final SessionHelper sessionHelper;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    public DashboardController(SessionHelper sessionHelper, EmployeeService employeeService, ProjectService projectService) {
        this.sessionHelper = sessionHelper;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        model.addAttribute("resourceLoad", employeeService.getResourceLoadCurrentMonth());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("projectsNeedingAttention", projectService.findProjectsNeedingAttention());
        return "dashboard";
    }

}
