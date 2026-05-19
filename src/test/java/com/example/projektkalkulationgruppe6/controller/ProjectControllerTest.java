package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.repository.ProjectRepository;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private SessionHelper sessionHelper;

    Project p1 = new Project(
            null,
            "Test1",
            "Beskrivelse",
            LocalDate.of(2026, 6, 1),
            LocalDate.of(2026,12,1),
            new Employee(1, "Lars", "test@test.dk", "1234", List.of("sql")),
            new BigDecimal("1200.00"),
            "Not Started",
            null,
            null,
            null);

    @Test
    void shouldShowProjectForm() throws Exception {

        Project p1Return = p1;
        p1Return.setProjectId(1);

        when(projectService.create(p1)).thenReturn(p1Return);
        when(projectService.findAllStatusList()).thenReturn(
                List.of("Not Started",
                "Estimate In Progress",
                "Estimate Done",
                "Actual Hours Done",
                "Rejected")
        );
        when(employeeService.findAll()).thenReturn(
                List.of(
                        new Employee(1, "Lars", "test@test.dk", "1234", List.of("sql"))
                )
        );

        mockMvc.perform(get("/home/create-project"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/project-form"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attribute("formTitle", "Create Project"))
                .andExpect(model().attribute("formAction", "/home/create-project"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("statusList",
                        List.of("Not Started",
                                "Estimate In Progress",
                                "Estimate Done",
                                "Actual Hours Done",
                                "Rejected")))
                .andExpect(model().attribute("submitLabel", "Create"))
                .andExpect(model().attribute("cancelUrl", "/home"));

    }



}