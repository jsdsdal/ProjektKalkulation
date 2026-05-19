package com.example.projektkalkulationgruppe6.controller;


import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.service.TaskService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task")

public class TaskController {

    private final TaskService taskService;
    private final SessionHelper sessionHelper;

    public TaskController(TaskService taskService, SessionHelper sessionHelper) {
        this.taskService = taskService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/create")
    public String showCreateForm(HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        model.addAttribute("task", new Task());
        model.addAttribute("formTitle", "Create task");
        model.addAttribute("formAction","/task/create");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/home");

        return "task/task-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        int projectId = (int) session.getAttribute("projectId");
        try {
            taskService.create(task, projectId);
            return "redirect:/task/list";
        } catch (InvalidTaskException | DatabaseOperationException ex) {
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Create task");
            model.addAttribute("formAction","/task/create");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("cancelUrl", "/home");
            model.addAttribute("errorMessage", ex.getMessage());

            return "task/task-form";
        }
    }

    @PostMapping("/{taskId}/assignee/add")
    public String addAssignee(@PathVariable int taskId,
                              @RequestParam int employeeId) {
        taskService.addAssigneeToTask(taskId, employeeId);
        return "redirect:/task/edit/" + taskId;
    }

    @PostMapping("/{taskId}/competence/add")
    public String addCompetence(@PathVariable int taskId,
                                @RequestParam int competenceId) {
        taskService.addCompetenceToTask(taskId, competenceId);
        return "redirect:/task/edit/" + taskId;
    }




}
