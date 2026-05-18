package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.exception.InvalidEmployeeException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;


    //GET

    @Test
    void showLoginFormReturnsLoginView() throws Exception{
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk()).andExpect(view().name("auth/login"));

    }

    @Test
    void showLoginFormContainsNoErrorMessage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errorMessage"));
    }


    //POST med gyldigt login

    @Test
    void loginWithValidCredentialsRedirects() throws Exception{
        Employee employee = new Employee(1, "Test User", "test@alphasolutions.com", "test123", List.of());
        when(employeeService.login("test@alphasolutions.com", "test123")).thenReturn(employee);

        mockMvc.perform(post("/login")
                .param("email", "test@alphasolutions.com")
                .param("password", "test123")).
                andExpect(status().is3xxRedirection());
    }

    @Test
    void loginWithValidCredentialsIsSavedInSession() throws Exception {
        Employee employee = new Employee(1, "Test User", "test@alphasolutions.com", "test123", List.of());
        when(employeeService.login("test@alphasolutions.com", "test123")).thenReturn(employee);

        mockMvc.perform(post("/login")
                        .param("email", "test@alphasolutions.com")
                        .param("password", "test123"))
                .andExpect(request().sessionAttribute("employeeId", 1));

    }


    //POST med fejlscenarier

    @Test
    void loginWrongPasswordReturnsLoginView() throws Exception {
        when(employeeService.login(any(), any()))
                .thenThrow(new InvalidEmployeeException("Wrong email or password."));

        mockMvc.perform(post("/login")
                        .param("email", "test@alphasolutions.com")
                        .param("password", "test123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void loginWrongPasswordShowsErrorMessage() throws Exception {
        when(employeeService.login(any(), any()))
                .thenThrow(new InvalidEmployeeException("Wrong email or password."));

        mockMvc.perform(post("/login")
                        .param("email", "test@alphasolutions.com")
                        .param("password", "test123"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Wrong email or password."));
    }

    @Test
    void loginWrongPasswordDoesNotSaveSession() throws Exception {
        when(employeeService.login(any(), any()))
                .thenThrow(new InvalidEmployeeException("Wrong email or password."));

        mockMvc.perform(post("/login")
                        .param("email", "test@alphasolutions.com")
                        .param("password", "forkert"))
                .andExpect(request().sessionAttributeDoesNotExist("employeeId"));
    }

    @Test
    void loginWithValidCredentialsRedirectsToHome() throws Exception {
        Employee employee = new Employee(1, "Test User", "test@alphasolutions.com", "test123", List.of());
        when(employeeService.login("test@alphasolutions.com", "test123")).thenReturn(employee);

        mockMvc.perform(post("/login")
                        .param("email", "test@alphasolutions.com")
                        .param("password", "test123"))
                .andExpect(redirectedUrl("/home"));


    }

}
