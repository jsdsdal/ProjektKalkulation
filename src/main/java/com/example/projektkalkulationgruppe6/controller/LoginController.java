package com.example.projektkalkulationgruppe6.controller;


import com.example.projektkalkulationgruppe6.exception.InvalidEmployeeException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final EmployeeService employeeService;

    public LoginController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String showLoginForm(){
        return "auth/login";
    }


    @PostMapping()
    public String login (@RequestParam String email,
                         @RequestParam String password,
                         HttpSession session, Model model){
        try {
            Employee employee = employeeService.login(email, password);
            session.setAttribute("employeeId", employee.getEmployeeId());
            return "redirect:/home";
        } catch (InvalidEmployeeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/login";
        }

    }






}
