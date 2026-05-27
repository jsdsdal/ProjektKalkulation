package com.example.projektkalkulationgruppe6.controller;



import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping()
public class DashboardController {

    private final SessionHelper sessionHelper;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    public DashboardController(SessionHelper sessionHelper, EmployeeService employeeService, ProjectService projectService) {
        this.sessionHelper = sessionHelper;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);

        int year = LocalDate.now().getYear();
        int quarter = (LocalDate.now().getMonthValue() - 1) / 3 + 1;

        model.addAttribute("resourceLoad", employeeService.getResourceLoadCurrentMonth());
        model.addAttribute("forecastedRevenue", projectService.getQuarterlyEstimatedRevenue(year, quarter));
        model.addAttribute("actualRevenue", projectService.getQuarterlyActualRevenue(year, quarter));
        model.addAttribute("annualRevenue", projectService.getAnnualRevenue(year));
        model.addAttribute("projectsNeedingAttention", projectService.getProjectsNeedingAttention());
        model.addAttribute("projects", projectService.findAllThisYearForGanttDiagram());

        return "dashboard";
    }

}
