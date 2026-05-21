package com.example.projektkalkulationgruppe6.controller;


import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
import com.example.projektkalkulationgruppe6.exception.TaskNotFoundException;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.TaskService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/task")

public class TaskController {

    private final TaskService taskService;
    private final SessionHelper sessionHelper;
    private final EmployeeService employeeService;

    public TaskController(TaskService taskService, SessionHelper sessionHelper, EmployeeService employeeService) {
        this.taskService = taskService;
        this.sessionHelper = sessionHelper;
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        model.addAttribute("tasks", taskService.findAllTask());
        model.addAttribute("projectId", session.getAttribute("projectId"));
        return "task/task-list";
    }


    @GetMapping("/create")
    public String showCreateForm(HttpSession session, Model model, @RequestParam(required = false) Integer projectId) {
        sessionHelper.isLoggedIn(session);
        model.addAttribute("task", new Task());
        model.addAttribute("formTitle", "Create task");
        model.addAttribute("formAction", "/task/create");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/home");
        model.addAttribute("allCompetencies", taskService.findAllCompetencies());
        model.addAttribute("allEmployees", employeeService.findAll());
        model.addAttribute("projectId", projectId);

        return "task/task-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @RequestParam int projectId, HttpSession session, Model model, @RequestParam(required = false) List<Integer> competenceId, @RequestParam(required = false) List<Integer> employeeId) {
        sessionHelper.isLoggedIn(session);
        try {
            taskService.create(task, projectId, competenceId, employeeId);
            return "redirect:/task/list";
        } catch (InvalidTaskException | DatabaseOperationException ex) {
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Create task");
            model.addAttribute("formAction", "/task/create");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("cancelUrl", "/home");
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("projectId", projectId);

            return "task/task-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, HttpSession session, Model model) {
        try {
        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("formTitle", "Edit task");
        model.addAttribute("formAction", "/task/edit/" + id);
        model.addAttribute("submitLabel", "Save");
        model.addAttribute("cancelUrl", "/task");
        model.addAttribute("allCompetencies", taskService.findAllCompetencies());
        model.addAttribute("allEmployees", employeeService.findAll());

        return "task/task-form";

    } catch(InvalidTaskException | TaskNotFoundException ex) {
        return "redirect:/task";
    }
}

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute Task task,
                         @RequestParam(required = false) List<Integer> competenceId,
                         @RequestParam(required = false) List<Integer> employeeId,
                         Model model) {
        task.setTaskId(id);
        try {
            taskService.update(task);
            return "redirect:/task";
        } catch (InvalidTaskException | TaskNotFoundException | DatabaseOperationException ex) {
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Edit task");
            model.addAttribute("formAction", "/task/edit/" + id);
            model.addAttribute("submitLabel", "Save");
            model.addAttribute("cancelUrl", "/task");
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            return "task/task-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        taskService.deleteById(id);
        return "redirect:/task/list";
    }

    @PostMapping("/{taskId}/assignee/add")
    public String addAssignee(@PathVariable int taskId,
                              @RequestParam int employeeId) {
        taskService.addAssigneeToTask(taskId, employeeId);
        return "redirect:/task/edit/" + taskId;
    }

    @PostMapping("/{taskId}/assignee/remove")
    public String removeAssignee(@PathVariable int taskId,
                                 @RequestParam int employeeId) {
        taskService.removeAssigneeFromTask(taskId, employeeId);
        return "redirect:/task/edit/" + taskId;
    }

    @PostMapping("/{taskId}/competence/add")
    public String addCompetence(@PathVariable int taskId,
                                @RequestParam int competenceId) {
        taskService.addCompetenceToTask(taskId, competenceId);
        return "redirect:/task/edit/" + taskId;
    }

    @PostMapping("/{taskId}/competence/remove")
    public String removeCompetence(@PathVariable int taskId,
                                   @RequestParam int competenceId) {
        taskService.removeCompetenceFromTask(taskId, competenceId);
        return "redirect:/task/edit/" + taskId;
    }




}
