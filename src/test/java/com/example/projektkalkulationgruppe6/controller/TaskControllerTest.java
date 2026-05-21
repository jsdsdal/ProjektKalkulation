package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.TaskService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private SessionHelper sessionHelper;

    Task mainTestTask = new Task(
            null,
            "Test Task Title",
            "Test Description",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 12),
            11.00,
            20.00,
            null,
            null,
            null,
            null,
            null
    );

    @Test
    void listShouldReturnTaskListView() throws Exception {
        when(taskService.findAllTask()).thenReturn(List.of(mainTestTask));

        mockMvc.perform(get("/task/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("task/task-list"))
                .andExpect(model().attributeExists("tasks"));
    }

    @Test
    void getCreateFormShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/task/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("task/task-form"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("formTitle", "Create task"))
                .andExpect(model().attribute("formAction", "/task/create"))
                .andExpect(model().attribute("submitLabel", "Create"));
    }

    @Test
    void createValidTaskShouldRedirectToList() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("projectId", 1);

        when(taskService.create(any(Task.class), eq(1), any(), any())).thenReturn(mainTestTask);

        mockMvc.perform(post("/task/create")
                        .session(session)
                        .param("projectId", "1")
                        .param("title", "Test Task")
                        .param("description", "Test Description")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-12")
                        .param("estimatedHours", "11.00")
                        .param("actualHours", "20.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/list"));
    }

    @Test
    void createInvalidTaskShouldReturnFormWithError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("projectId", 1);

        when(taskService.create(any(Task.class), eq(1), any(), any()))
                .thenThrow(new InvalidTaskException("Title is required"));

        mockMvc.perform(post("/task/create")
                        .session(session)
                        .param("projectId", "1")
                        .param("title", "")
                        .param("description", "Test Description")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-12"))
                .andExpect(status().isOk())
                .andExpect(view().name("task/task-form"))
                .andExpect(model().attribute("errorMessage", "Title is required"));
    }

    @Test
    void deleteTaskShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/task/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/list"));
    }
}