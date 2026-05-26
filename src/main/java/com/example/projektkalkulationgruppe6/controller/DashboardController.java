package com.example.projektkalkulationgruppe6.controller;



import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class DashboardController {

    private final SessionHelper sessionHelper;
    private final EmployeeService employeeService;

    public DashboardController(SessionHelper sessionHelper, EmployeeService employeeService) {
        this.sessionHelper = sessionHelper;
        this.employeeService = employeeService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        model.addAttribute("resourceLoad", employeeService.getResourceLoadCurrentMonth());
        return "dashboard";
    }

}
