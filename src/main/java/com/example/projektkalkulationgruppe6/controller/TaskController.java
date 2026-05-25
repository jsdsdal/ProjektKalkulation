package com.example.projektkalkulationgruppe6.controller;


import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
import com.example.projektkalkulationgruppe6.exception.TaskNotFoundException;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import com.example.projektkalkulationgruppe6.service.TaskService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;


@Controller
@RequestMapping("/projects/{projectId}/tasks")

public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final SessionHelper sessionHelper;
    private final EmployeeService employeeService;

    public TaskController(TaskService taskService, ProjectService projectService, SessionHelper sessionHelper, EmployeeService employeeService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.sessionHelper = sessionHelper;
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String list(@PathVariable int projectId, Model model) {
        model.addAttribute("tasks", taskService.findAllTask());
        model.addAttribute("projectId", projectId);
        return "tasks/task-list";
    }

    @GetMapping("/{taskId}")
    public String showTask(@PathVariable int projectId, @PathVariable int taskId, Model model) {

        Project project = projectService.findById(projectId);

        int mainProjectId;

        if (project.getParentProjectId() == null) {
            mainProjectId = project.getProjectId();
        } else {
            mainProjectId = project.getParentProjectId();
        }

        model.addAttribute("task", taskService.findById(taskId));

        model.addAttribute("closeUrl", "/projects/" + mainProjectId);

        return "tasks/task-detail";
    }


    @GetMapping("/create-task")
    public String showCreateForm(@PathVariable int projectId, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        model.addAttribute("task", new Task());
        model.addAttribute("formTitle", "Create task");
        model.addAttribute("formAction", "/task/create");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/home");
        model.addAttribute("allCompetencies", taskService.findAllCompetencies());
        model.addAttribute("allEmployees", employeeService.findAll());
        model.addAttribute("projectId", projectId);

        return "tasks/task-form";
    }

    @PostMapping("/create-task")
    public String create(@PathVariable int projectId, @ModelAttribute Task task, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        try {
            taskService.create(task, projectId, new ArrayList<>(), new ArrayList<>());
            return "redirect:/projects/" + projectId + "/tasks/list";
        } catch (InvalidTaskException | DatabaseOperationException ex) {
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Create task");
            model.addAttribute("formAction", "/projects/" + projectId + "/tasks/create-task");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("cancelUrl", "/home");
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("projectId", projectId);

            return "tasks/task-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int projectId, @PathVariable int id, HttpSession session, Model model) {
        try {
            model.addAttribute("task", taskService.findById(id));
            model.addAttribute("formTitle", "Edit task");
            model.addAttribute("formAction", "/projects/" + projectId + "/tasks/edit/" + id);
            model.addAttribute("submitLabel", "Save");
            model.addAttribute("cancelUrl", "/task");
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());

            return "tasks/task-form";

        } catch (InvalidTaskException | TaskNotFoundException ex) {
            return "redirect:/projects/" + projectId + "/tasks/list";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int projectId, @PathVariable int id, @ModelAttribute Task task, Model model) {
        task.setTaskId(id);
        try {
            taskService.update(task);
            return "redirect:/projects/" + projectId + "/tasks/list";
        } catch (InvalidTaskException | TaskNotFoundException | DatabaseOperationException ex) {
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Edit task");
            model.addAttribute("formAction", "/projects/" + projectId + "/tasks/edit/" + id);
            model.addAttribute("submitLabel", "Save");
            model.addAttribute("cancelUrl", "/task");
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());

            return "tasks/task-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int projectId, @PathVariable int id) {
        taskService.deleteById(id);
        return "redirect:/projects/" + projectId + "/tasks/list";
    }

    @PostMapping("/{taskId}/assignee/add")
    public String addAssignee(@PathVariable int projectId, @PathVariable int taskId,
                              @RequestParam int employeeId) {
        taskService.addAssigneeToTask(taskId, employeeId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit";
    }

    @PostMapping("/{taskId}/assignee/remove")
    public String removeAssignee(@PathVariable int projectId, @PathVariable int taskId,
                                 @RequestParam int employeeId) {
        taskService.removeAssigneeFromTask(taskId, employeeId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit";
    }

    @PostMapping("/{taskId}/competence/add")
    public String addCompetence(@PathVariable int projectId, @PathVariable int taskId,
                                @RequestParam int competenceId) {
        taskService.addCompetenceToTask(taskId, competenceId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit";
    }

    @PostMapping("/{taskId}/competence/remove")
    public String removeCompetence(@PathVariable int projectId, @PathVariable int taskId,
                                   @RequestParam int competenceId) {
        taskService.removeCompetenceFromTask(taskId, competenceId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit";
    }


}