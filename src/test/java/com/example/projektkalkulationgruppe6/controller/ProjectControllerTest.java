package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.repository.ProjectRepository;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    Project p2 = new Project(
            null,
            "Test2",
            "Beskrivelse2",
            LocalDate.of(2026, 7, 1),
            LocalDate.of(2026,11,1),
            new Employee(1, "Lars", "test@test.dk", "1234", List.of("sql")),
            new BigDecimal("1100.00"),
            "Estimate In Progress",
            null,
            null,
            null);


    @Test
    void shouldListProjects() throws Exception {
        Project p1Return = p1;
        p1Return.setProjectId(1);
        Project p2Return = p2;
        p2Return.setProjectId(2);

        when(projectService.findAll()).thenReturn(
                List.of(p1Return, p2Return));

        mockMvc.perform(get("/projects/table"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("projects", hasSize(2)))
                .andExpect(model().attribute("projects", hasItem(
                        hasProperty("projectId", is(1)))))
                .andExpect(model().attribute("projects", hasItem(
                        hasProperty("projectId", is(2)))))
                .andExpect(model().attribute("viewProjectUrl", "/projects/"))
                .andExpect(model().attribute("createProjectUrl", "/projects/create-project"))
                .andExpect(model().attribute("editProjectUrl", "/projects/edit-project"))
                .andExpect(model().attribute("deleteProjectUrl", "/projects/delete-project"));
    }

    @Test
    void shouldShowProjectForm() throws Exception {

        when(projectService.findAllStatusList()).thenReturn(
                List.of("Not Started",
                "Estimate In Progress",
                "Estimate Done",
                "Actual Hours Done",
                "Rejected")
        );
        when(employeeService.findAll()).thenReturn(
                List.of(
                        new Employee(1, "Lars", "test@test.dk", "1234", List.of("sql")))
        );

        mockMvc.perform(get("/projects/create-project"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/project-form"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attribute("formTitle", "Create Project"))
                .andExpect(model().attribute("formAction", "/projects/create-project"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("statusList",
                        List.of("Not Started",
                                "Estimate In Progress",
                                "Estimate Done",
                                "Actual Hours Done",
                                "Rejected")))
                .andExpect(model().attribute("submitLabel", "Create"))
                .andExpect(model().attribute("cancelUrl", "/projects/table"));

    }

    @Test
    void shouldCreateProject() throws Exception {
        Project p1Return = p1;
        p1Return.setProjectId(1);

        when(projectService.create(any(Project.class))).thenReturn(p1Return);
        when(employeeService.findById(1)).thenReturn(
                new Employee(1, "Lars", "test@test.dk", "1234", List.of("sql"))
        );

        mockMvc.perform(post("/projects/create-project")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Test1")
                .param("description", "Beskrivelse")
                .param("startDate", "2026-06-01")
                .param("endDate", "2026-12-01")
                .param("hourPrice", "1200.00")
                .param("status", "Not Started")
                .param("projectOwnerId", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/projects/project-list"));

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectService).create(captor.capture());

        Project captured = captor.getValue();
        assertThat(captured.getTitle()).isEqualTo("Test1");
        assertThat(captured.getDescription()).isEqualTo("Beskrivelse");
        assertThat(captured.getStartDate()).isEqualTo(LocalDate.of(2026,6,1));
        assertThat(captured.getEndDate()).isEqualTo(LocalDate.of(2026,12,1));
        assertThat(captured.getHourPrice()).isEqualByComparingTo(new BigDecimal("1200.00"));
        assertThat(captured.getProjectOwner().getEmployeeId()).isEqualTo(1);
    }

    @Test
    void shouldCatchInvalidProjectExceptionAndShowAttributes() throws Exception {
        Project p1Return = p1;
        p1Return.setProjectId(1);

        doThrow(new InvalidProjectException("Project problem")).when(projectService).create(any(Project.class));

        when(employeeService.findById(1)).thenReturn(
                new Employee(1, "Lars", "test@test.dk", "1234", List.of("sql"))
        );

        when(projectService.findAllStatusList()).thenReturn(
                List.of("Not Started",
                        "Estimate In Progress",
                        "Estimate Done",
                        "Actual Hours Done",
                        "Rejected")
        );

        mockMvc.perform(post("/projects/create-project")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Test1")
                        .param("description", "Beskrivelse")
                        .param("startDate", "2026-06-01")
                        .param("endDate", "2026-12-01")
                        .param("hourPrice", "1200.00")
                        .param("status", "Not Started")
                        .param("projectOwnerId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/project-form"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attribute("errorMessage", "Project problem"))
                .andExpect(model().attribute("formTitle", "Create Project"))
                .andExpect(model().attribute("formAction", "/projects/create-project"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("statusList",
                        List.of("Not Started",
                                "Estimate In Progress",
                                "Estimate Done",
                                "Actual Hours Done",
                                "Rejected")))
                .andExpect(model().attribute("submitLabel", "Create"))
                .andExpect(model().attribute("cancelUrl", "/projects/table"));

                ;
    }


    @Test
    void deleteValidIdRedirectsToHome() throws Exception {
        mockMvc.perform(post("/projects/1/delete")
                        .sessionAttr("employeeId", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("projects/project-list"));
    }






}