package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SessionHelper sessionHelper;

    public EmployeeController(EmployeeService employeeService, SessionHelper sessionHelper) {
        this.employeeService = employeeService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        model.addAttribute("employees", employeeService.findAll());
        return "employee/employee-list";
    }
}
