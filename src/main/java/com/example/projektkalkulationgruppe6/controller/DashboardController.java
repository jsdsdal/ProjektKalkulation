package com.example.projektkalkulationgruppe6.controller;


import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class DashboardController {

    private final SessionHelper sessionHelper;

    public DashboardController(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        sessionHelper.isLoggedIn(session);
        return "dashboard";
    }


}
