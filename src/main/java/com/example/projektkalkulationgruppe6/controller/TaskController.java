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
import java.util.List;


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

        int rootProjectId = projectService.findRootProjectIdById(projectId);

        model.addAttribute("task", taskService.findById(taskId));

        model.addAttribute("closeUrl", "/projects/" + rootProjectId);

        return "tasks/task-detail";
    }


    @GetMapping("/create-task")
    public String showCreateForm(@PathVariable int projectId, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        int rootProjectId = projectService.findRootProjectIdById(projectId);
        model.addAttribute("task", new Task());
        model.addAttribute("formTitle", "Create task");
        model.addAttribute("formAction", "/projects/" + projectId + "/tasks/create-task");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
        model.addAttribute("allCompetencies", taskService.findAllCompetencies());
        model.addAttribute("allEmployees", employeeService.findAll());
        model.addAttribute("projectId", projectId);

        return "tasks/task-form";
    }


    @PostMapping("/create-task")
    public String createTask(@PathVariable int projectId, @ModelAttribute Task task, HttpSession session, Model model,
                             @RequestParam(required = false) List<Integer> competenceId,
                             @RequestParam(required = false) List<Integer> employeeId) {
        sessionHelper.isLoggedIn(session);
        try {
            int rootProjectId = projectService.findRootProjectIdById(projectId);
            taskService.create(task, projectId,
                    competenceId != null ? competenceId : List.of(),
                    employeeId != null ? employeeId : List.of());
            return "redirect:/projects/" + rootProjectId;
        } catch (InvalidTaskException | DatabaseOperationException ex) {
            int rootProjectId = projectService.findRootProjectIdById(projectId);
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Create task");
            model.addAttribute("formAction", "/projects/" + projectId + "/tasks/create-task");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("projectId", projectId);

            return "tasks/task-form";
        }
    }


    @GetMapping("/{parentTaskId}/create-task")
    public String showSubTaskCreateForm(@PathVariable int projectId, @PathVariable int parentTaskId,
                                        HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);
        int rootProjectId = projectService.findRootProjectIdById(projectId);
        model.addAttribute("task", new Task());
        model.addAttribute("formTitle", "Create task");
        model.addAttribute("formAction", "/projects/" + projectId + "/tasks/" + parentTaskId +  "/create-task");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
        model.addAttribute("allCompetencies", taskService.findAllCompetencies());
        model.addAttribute("allEmployees", employeeService.findAll());
        model.addAttribute("projectId", projectId);

        return "tasks/task-form";
    }

    @PostMapping("/{parentTaskId}/create-task")
    public String createSubTask(@PathVariable int projectId, @PathVariable int parentTaskId, @ModelAttribute Task task,
                                HttpSession session, Model model,
                                @RequestParam(required = false) List<Integer> competenceId,
                                @RequestParam(required = false) List<Integer> employeeId) {
        sessionHelper.isLoggedIn(session);
        try {
            int rootProjectId = projectService.findRootProjectIdById(projectId);

            task.setParentTaskId(parentTaskId);
            taskService.create(task, projectId,
                competenceId != null ? competenceId : List.of(),
                    employeeId != null ? employeeId : List.of());

            return "redirect:/projects/" + rootProjectId;
        } catch (InvalidTaskException ex) {
            int rootProjectId = projectService.findRootProjectIdById(projectId);
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Create task");
            model.addAttribute("formAction", "/projects/" + projectId + "/tasks/" + parentTaskId +  "/create-task");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("projectId", projectId);

            return "tasks/task-form";
        }
    }

    @GetMapping("/{id}/edit-task")
    public String showEditForm(@PathVariable int projectId, @PathVariable int id, HttpSession session, Model model) {

        int rootProjectId = projectService.findRootProjectIdById(projectId);

        try {
            model.addAttribute("task", taskService.findById(id));
            model.addAttribute("formTitle", "Edit task");
            model.addAttribute("formAction", "/projects/" + projectId + "/tasks/" + id + "/edit-task");
            model.addAttribute("submitLabel", "Save");
            model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());

            return "tasks/task-form";

        } catch (InvalidTaskException ex) {
            return "redirect:/projects/" + rootProjectId;
        }
    }

    @PostMapping("/{id}/edit-task")
    public String update(@PathVariable int projectId, @PathVariable int id,
                         @ModelAttribute Task task,
                         @RequestParam(required = false) List<Integer> competenceId,
                         @RequestParam(required = false) List<Integer> employeeId,
                         Model model) {
        task.setTaskId(id);
        int rootProjectId = projectService.findRootProjectIdById(projectId);

        try {
            taskService.update(task);

            // Sync competencies
            Task current = taskService.findById(id);
            if (current.getCompetencies() != null) {
                for (String title : current.getCompetencies()) {
                    taskService.findAllCompetencies().stream()
                            .filter(c -> c.title().equals(title))
                            .findFirst()
                            .ifPresent(c -> taskService.removeCompetenceFromTask(id, c.id()));
                }
            }
            if (competenceId != null) {
                for (int cId : competenceId) taskService.addCompetenceToTask(id, cId);
            }

            // Sync assignees
            if (current.getAssignees() != null) {
                for (var assignee : current.getAssignees()) {
                    taskService.removeAssigneeFromTask(id, assignee.getEmployeeId());
                }
            }
            if (employeeId != null) {
                for (int eId : employeeId) taskService.addAssigneeToTask(id, eId);
            }

            return "redirect:/projects/" + rootProjectId;
        } catch (InvalidTaskException | TaskNotFoundException | DatabaseOperationException ex) {
            model.addAttribute("task", task);
            model.addAttribute("formTitle", "Edit task");
            model.addAttribute("formAction", "/projects/" + projectId + "/tasks/" + id + "/edit-task");
            model.addAttribute("submitLabel", "Save");
            model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
            model.addAttribute("allCompetencies", taskService.findAllCompetencies());
            model.addAttribute("allEmployees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            return "tasks/task-form";
        }
    }

    @PostMapping("/{id}/delete-task")
    public String delete(@PathVariable int projectId, @PathVariable int id) {
        taskService.deleteById(id);
        int rootProjectId = projectService.findRootProjectIdById(projectId);
        return "redirect:/projects/" + rootProjectId;
    }

    @PostMapping("/{taskId}/assignee/add")
    public String addAssignee(@PathVariable int projectId, @PathVariable int taskId,
                              @RequestParam int employeeId) {
        taskService.addAssigneeToTask(taskId, employeeId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit-task";
    }

    @PostMapping("/{taskId}/assignee/remove")
    public String removeAssignee(@PathVariable int projectId, @PathVariable int taskId,
                                 @RequestParam int employeeId) {
        taskService.removeAssigneeFromTask(taskId, employeeId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit-task";
    }

    @PostMapping("/{taskId}/competence/add")
    public String addCompetence(@PathVariable int projectId, @PathVariable int taskId,
                                @RequestParam int competenceId) {
        taskService.addCompetenceToTask(taskId, competenceId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit-task";
    }

    @PostMapping("/{taskId}/competence/remove")
    public String removeCompetence(@PathVariable int projectId, @PathVariable int taskId,
                                   @RequestParam int competenceId) {
        taskService.removeCompetenceFromTask(taskId, competenceId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/edit-task";
    }


}