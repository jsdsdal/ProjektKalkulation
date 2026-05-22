package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.exception.ProjectNotFoundException;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.repository.ProjectRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskService taskService;



    public ProjectService(ProjectRepository projectRepository, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.taskService = taskService;
    }

    public List<Project> findAll() {
        try {
            return projectRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Projects could not be loaded.", ex);
        }
    }

    public Project create(Project project) {
        validateProject(project);

        try {
            return projectRepository.insert(project);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The project was not created.", ex);
        }
    }

    public Project findById(int id) {
        validateId(id);

        Project project;

        try {
            project = getTreeById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The Project could not be loaded.", ex);
        }

        if (project == null) {
            throw new ProjectNotFoundException(id);
        }

        return project;


    }

    private Project getTreeById(int id) {
        List<Project> projects = new ArrayList<>();



        try {
            projects = projectRepository.findParentAndChildrenById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The Project could not be loaded.", ex);
        }



        if (projects.isEmpty()) {
            throw new ProjectNotFoundException(id);
        }

        for (Project p : projects) {
            List<Task> tasks = taskService.getTreesByProjectId(p.getProjectId());
            p.setTasks(tasks);
        }

        Project parentProject = projects.getFirst();

        projects.removeFirst();

        parentProject.setSubProjects(projects);

        return parentProject;

    }

    public List<String> findAllStatusList() {

        List<String> statusList;

        try {
            statusList = projectRepository.findAllStatusList();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Status list could not be loaded.", ex);
        }

        if (statusList.isEmpty()) {
            throw new DatabaseOperationException(
                    "Error: Status list could not be loaded.",
                    new Throwable("List result is empty"));
        }

        return statusList;
    }

    private void validateId(int id) {
        if (id <= 0) {
                throw new InvalidProjectException("ID must be a positive number");
            }

    }

    private void validateProject(Project project) {
        if (project == null) {
            throw new InvalidProjectException("Project is mandatory");
        }

        String title = project.getTitle();
        if (title == null || title.isBlank()) {
            throw new InvalidProjectException("Project Title is a mandatory field");
        }

        String status = project.getStatus();
        if (status == null || status.isBlank() ) {
            throw new InvalidProjectException("Project Status is a mandatory field");
        }

        if (project.getProjectOwner().getEmployeeId() == null) {
            throw new InvalidProjectException("Project Owner is a mandatory field");
        }

        //OBS: Mangler validering af project Owner

    }

    public Project createSubproject(Project subproject, int parentProjectId) {
        Project parent = projectRepository.findById(parentProjectId);

        // tjek om projekt findes
        if (parent == null) {
            throw new InvalidProjectException("Parent project not found: " + parentProjectId);
        }

        // ingen børnebørn
        if (parent.getParentProjectId() != null) {
            throw new InvalidProjectException("Cannot create subproject under existing subproject.");
        }

        // må ikke starte før parent project
        if (parent.getStartDate() != null && subproject.getStartDate() != null
        && subproject.getStartDate().isBefore(parent.getStartDate())) {
            throw new InvalidProjectException("Subproject cannot start before the parent project.");
        }

        // må ikke slutte efter parent project
        if (parent.getEndDate() != null && subproject.getEndDate() != null
            && subproject.getEndDate().isAfter(parent.getEndDate())) {
            throw new InvalidProjectException("Subproject cannot end after the parent project.");
        }

        subproject.setParentProjectId(parentProjectId);
        return create(subproject);
    }

    public void delete(int projectId){
        if(projectId<=0){
            throw new InvalidProjectException("ProjectID needs to be a positive number");
        }

        boolean deleted = projectRepository.deleteById(projectId);

        if(!deleted){
            throw new ProjectNotFoundException(projectId);
        }

    }


    public Project update(int projectId, Project project) {
        if (projectId <= 0) {
            throw new InvalidProjectException("ProjectID needs to be a positive number");
        }

        validateProject(project);

        boolean updated = projectRepository.updateById(projectId, project);

        if (!updated) {
            throw new ProjectNotFoundException(projectId);
        }

        return projectRepository.findById(projectId);
    }


    public int getRemainingDaysTillDeadlineForProject(int id) {
        Project project = projectRepository.findById(id);

        if (project == null) {
            throw new ProjectNotFoundException(id);
        }

        if (project.getEndDate() == null) {
            throw new InvalidProjectException("Project has no deadline.");
        }

        LocalDate today = LocalDate.now();
        int remainingDays = (int) ChronoUnit.DAYS.between(today, project.getEndDate());

        return remainingDays;
    }

    //Tag stilling til om denne skaber noget værdi
    public String getDeadlineStatus(int id) {

        Project project = projectRepository.findById(id);

        if (project == null) {
            throw new ProjectNotFoundException(id);
        }

        if (project.getEndDate() == null) {
            throw new InvalidProjectException("Project has no deadline.");
        }

        LocalDate today = LocalDate.now();
        long daysRemaining =
                ChronoUnit.DAYS.between(today, project.getEndDate());

        if (daysRemaining < 0) {
            return "Behind";
        }

        if (daysRemaining <= 2) {
            return "Needs attention";
        }

        return "On time";
    }







}
