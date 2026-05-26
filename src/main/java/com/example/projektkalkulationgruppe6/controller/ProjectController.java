package com.example.projektkalkulationgruppe6.controller;

import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.exception.ProjectNotFoundException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.service.EmployeeService;
import com.example.projektkalkulationgruppe6.service.ProjectService;
import com.example.projektkalkulationgruppe6.service.TaskService;
import com.example.projektkalkulationgruppe6.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final SessionHelper sessionHelper;
    private final PathMatcher pathMatcher;


    public ProjectController(ProjectService projectService, EmployeeService employeeService, TaskService taskService, SessionHelper sessionHelper, PathMatcher pathMatcher) {
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.sessionHelper = sessionHelper;
        this.pathMatcher = pathMatcher;
    }
//TEST

    @GetMapping("/table")
    public String list(Model model, HttpSession session) {
        sessionHelper.isLoggedIn(session);

        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("viewProjectUrl", "/projects/");
        model.addAttribute("createProjectUrl", "/projects/create-project");
        model.addAttribute("editProjectUrl", "/projects/{id}/edit-project");
        model.addAttribute("deleteProjectUrl", "/projects/{id}/delete-project");
        model.addAttribute("tasksDueThisWeekCount", taskService.getCountOfAllTasksDueThisWeek());
        model.addAttribute("activeProjects", projectService.getActiveProjectCount());
        model.addAttribute("needsAttention", projectService.getProjectsNeedingAttentionCount());
        model.addAttribute("totalTasks", taskService.getAllTaskCount());
        model.addAttribute("estimatedHoursThisWeek", taskService.getEstimatedHoursThisWeek());


        return "projects/project-list";

    }

    @GetMapping("/{id}")
    public String read(@PathVariable int id, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);

        Project project = projectService.findById(id);

        int rootProjectId = projectService.findRootProjectIdById(id);

        model.addAttribute("project", project);
        model.addAttribute("rootProjectId", rootProjectId);
        model.addAttribute("createSubProjectUrl", "/projects/" + id + "/create-project");

        model.addAttribute("readSubProjectAndTaskBaseUrl", "/projects/");
        model.addAttribute("readTaskUrlSegment", "/tasks/");

        model.addAttribute("updateSubProjectUrl", "/projects/{id}/edit-project");
        model.addAttribute("deleteSubProjectUrl", "/projects/{id}/delete-project");


        model.addAttribute("createTaskUrl", "/projects/" + id + "/tasks/create-task");
        model.addAttribute("updateTaskUrl", "/projects/" + id + "/tasks/{id}/edit-task");
        model.addAttribute("deleteTaskUrl", "/projects/" + id + "/tasks/{id}/delete-task");

        model.addAttribute("closeUrl", "/projects/" + rootProjectId);

        model.addAttribute("totalEstimatedHours", projectService.getTotalEstimatedHoursByProjectId(id));
        model.addAttribute("totalActualHours", projectService.getTotalActualHoursByProjectId(id));
        model.addAttribute("remainingHours", projectService.getRemainingHoursByProjectId(id));

        if(project.getParentProjectId()== null) {
            return "projects/project-detail";
        }

        return "projects/sub-project-detail";
    }

    @GetMapping("/{id}/overview")
    public String overview(@PathVariable int id, Model model, HttpSession session){
        model.addAttribute("daysUntilDeadline", projectService.getRemainingDaysTillDeadlineForProject(id));
        model.addAttribute("totalTasks", taskService.getTaskCountByProjectId(id));
        model.addAttribute("completedTasks", taskService.getCompletedTaskCountByProjectId(id));


        return "projects/project-overview";
    }

    @GetMapping("/create-project")
    public String showCreateForm(Model model, HttpSession session) {
        sessionHelper.isLoggedIn(session);

        model.addAttribute("project", new Project());
        model.addAttribute("formTitle", "Create Project");
        model.addAttribute("statusList", projectService.findAllStatusList());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("formAction", "/projects/create-project");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/projects/table");
        return "projects/project-form";
    }

    @PostMapping("/create-project")
    public String create(@ModelAttribute Project project, @RequestParam int projectOwnerId, Model model, HttpSession session) {
        sessionHelper.isLoggedIn(session);

        try {
            Employee projectOwner = employeeService.findById(projectOwnerId);
            project.setProjectOwner(projectOwner);
            projectService.create(project);
            return "redirect:/projects/table";
        } catch (InvalidProjectException ex) {
            model.addAttribute("project", project);
            model.addAttribute("formTitle", "Create Project");
            model.addAttribute("statusList", projectService.findAllStatusList());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("formAction", "/projects/create-project");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("cancelUrl", "/projects/table");
            return "projects/project-form";
        }
    }

    @GetMapping("/{parentProjectId}/create-project")
    public String showCreateSubProjectForm(Model model, HttpSession session, @PathVariable int parentProjectId) {
        sessionHelper.isLoggedIn(session);

        model.addAttribute("project", new Project());
        model.addAttribute("formTitle", "Create Sub Project");
        model.addAttribute("statusList", projectService.findAllStatusList());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("formAction", "/projects/" + parentProjectId + "/create-project");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("cancelUrl", "/projects/" + parentProjectId);
        return "projects/project-form";
    }

    @PostMapping("/{parentProjectId}/create-project")
    public String createSubProject(@ModelAttribute Project project, @PathVariable int parentProjectId,
                                   @RequestParam int projectOwnerId, Model model, HttpSession session) {

        sessionHelper.isLoggedIn(session);

        try {
            Employee projectOwner = employeeService.findById(projectOwnerId);
            project.setProjectOwner(projectOwner);
            project.setParentProjectId(parentProjectId);
            projectService.create(project);
            return "redirect:/projects/" + parentProjectId;
        } catch (InvalidProjectException ex) {
            model.addAttribute("project", project);
            model.addAttribute("formTitle", "Create Sub Project");
            model.addAttribute("statusList", projectService.findAllStatusList());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("formAction", "/projects/" + parentProjectId + "/create-project");
            model.addAttribute("submitLabel", "Create");
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("cancelUrl", "/projects/" + parentProjectId);
            return "projects/project-form";
        }
    }

    @PostMapping("/{id}/delete-project")
    public String delete(@PathVariable int id, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);

        int rootProjectId = projectService.findRootProjectIdById(id);

        try {
            projectService.delete(id);
            if (id != rootProjectId) {
                return "redirect:/projects/" + rootProjectId;
            } else {
                return "redirect:/projects/table";
            }

        } catch (ProjectNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/404";
        } catch (InvalidProjectException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/400";
        }
    }

    @GetMapping("/{id}/edit-project")
    public String showEditForm(@PathVariable int id, HttpSession session, Model model) {
        sessionHelper.isLoggedIn(session);

        Project project = projectService.findById(id);

        int rootProjectId = projectService.findRootProjectIdById(id);

        model.addAttribute("project", project);
        model.addAttribute("formTitle", "Edit Project");
        model.addAttribute("formAction", "/projects/" + id + "/edit-project");
        model.addAttribute("submitLabel", "Save");

        if (rootProjectId != id) {
            model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
        } else {
            model.addAttribute("cancelUrl", "/projects/table");
        }

        model.addAttribute("statusList", projectService.findAllStatusList());
        model.addAttribute("employees", employeeService.findAll());

        return "projects/project-form";
    }

    @PostMapping("/{id}/edit-project")
    public String edit(@PathVariable int id,
                       @ModelAttribute Project project,
                       HttpSession session,
                       Model model) {
        sessionHelper.isLoggedIn(session);

        int rootProjectId = projectService.findRootProjectIdById(id);

        try {
            int employeeId = (int) session.getAttribute("employeeId");
            project.setProjectOwner(employeeService.findById(employeeId));
            projectService.update(id, project);

            if (rootProjectId != id) {
                return "redirect:/projects/" + rootProjectId;
            } else {
                return "redirect:/projects/table";
            }

        } catch (ProjectNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/404";
        } catch (InvalidProjectException ex) {
            model.addAttribute("project", project);
            model.addAttribute("formTitle", "Edit Project");
            model.addAttribute("formAction", "/projects/" + id + "/edit-project");
            model.addAttribute("submitLabel", "Save");

            if (rootProjectId != id) {
                model.addAttribute("cancelUrl", "/projects/" + rootProjectId);
            } else {
                model.addAttribute("cancelUrl", "/projects/table");
            }

            model.addAttribute("statusList", projectService.findAllStatusList());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            return "projects/project-form";
        }
    }





}
