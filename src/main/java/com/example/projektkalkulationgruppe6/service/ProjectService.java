package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.exception.ProjectNotFoundException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.repository.ProjectRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskService taskService;
    private final EmployeeService employeeService;


    public ProjectService(ProjectRepository projectRepository, TaskService taskService, EmployeeService employeeService) {
        this.projectRepository = projectRepository;
        this.taskService = taskService;
        this.employeeService = employeeService;
    }

    public List<Project> findAll() {
        try {
            return projectRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Projects could not be loaded.", ex);
        }
    }

    public Project create(Project project, Integer projectOwnerId) {
        validateProject(project, projectOwnerId);
        Employee projectOwner = employeeService.findById(projectOwnerId);
        project.setProjectOwner(projectOwner);

        try {
            return projectRepository.insert(project);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The project was not created.", ex);
        }
    }

    public int findRootProjectIdById(int id) {

        Project project = findById(id);

        int rootProjectId;

        if (project.getParentProjectId() == null) {
            rootProjectId = project.getProjectId();
        } else {
            rootProjectId = project.getParentProjectId();
        }
        return rootProjectId;
    }

    public Project findById(int id) {
        validateId(id);

        Project project = projectRepository.findById(id);
        int employeeId = project.getProjectOwner().getEmployeeId();
        project.setProjectOwner(employeeService.findById(employeeId));

        if (project == null) {
            throw new ProjectNotFoundException(id);
        }
        
        if (project.getParentProjectId() != null) {
            return project;
        }

        try {
            project = getTreeById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The Project could not be loaded.", ex);
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
            int employeeId = p.getProjectOwner().getEmployeeId();
            p.setProjectOwner(employeeService.findById(employeeId));
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

    private void validateProject(Project project, Integer projectOwnerId) {
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

        if (project.getStartDate() == null) {
            throw new InvalidProjectException("Start Date is required");
        }

        if (project.getEndDate() == null) {
            throw new InvalidProjectException("End date is required");
        }

        if(project.getEndDate().isBefore(project.getStartDate())) {
            throw new InvalidProjectException("End Date cannot be before Start Date");
        }

        if (projectOwnerId == null) {
            throw new InvalidProjectException("Project Owner is a mandatory field");
        }

        //OBS: Mangler validering af project Owner

    }

    public Project createSubproject(Project subproject, int parentProjectId, Integer projectOwnerId) {
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
            throw new InvalidProjectException("Subproject cannot start before the parent project. Parent project " +
                    "start date is " + parent.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-uuuu")));
        }

        // må ikke slutte efter parent project
        if (parent.getEndDate() != null && subproject.getEndDate() != null
            && subproject.getEndDate().isAfter(parent.getEndDate())) {
            throw new InvalidProjectException("Subproject cannot end after the parent project. Parent project " +
                    "end date is " + parent.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-uuuu")));
        }

        subproject.setParentProjectId(parentProjectId);
        return create(subproject, projectOwnerId);
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


    public Project update(int projectId, Project project, Integer projectOwnerId) {
        validateId(projectId);

        validateProject(project, projectOwnerId);


        Employee projectOwner = employeeService.findById(projectOwnerId);
        project.setProjectOwner(projectOwner);

        boolean updated = projectRepository.updateById(projectId, project);

        if (!updated) {
            throw new ProjectNotFoundException(projectId);
        }

        return projectRepository.findById(projectId);
    }


    // STATS
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

    // aktuel indkomst
    public BigDecimal getActualRevenueForProject(int projectId) {
        Project project = projectRepository.findById(projectId);
        if (project == null) throw new ProjectNotFoundException(projectId);
        if (project.getHourPrice() == null) return BigDecimal.ZERO;
        double actualHours = projectRepository.findTotalActualHoursByProjectId(projectId);
        return project.getHourPrice().multiply(BigDecimal.valueOf(actualHours));
    }


    // forventet indtægt til projekt
    public BigDecimal getExpectedRevenueForProject(int projectId) {
        Project project = projectRepository.findById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException(projectId);
        }
        if (project.getHourPrice() == null) return BigDecimal.ZERO;

        double totalHours = projectRepository.findTotalEstimatedHoursByProjectId(projectId);
        return project.getHourPrice().multiply(BigDecimal.valueOf(totalHours));
    }

    // Hjælpemetode til kvartal
    private boolean isInQuarter(LocalDate date, int year, int quarter) {
        if (date == null) return false;
        LocalDate start = LocalDate.of(year, (quarter - 1) * 3 + 1, 1);
        LocalDate end = start.plusMonths(3).minusDays(1);
        return !date.isBefore(start) && !date.isAfter(end);
    }

    // Est indkomst (kvartal)
    public BigDecimal getQuarterlyEstimatedRevenue(int year, int quarter) {
        List<Project> projects = projectRepository.findAll();
        if (projects == null) return BigDecimal.ZERO;

        BigDecimal estimated = BigDecimal.ZERO;

        for (Project p : projects) {
            if (p.getHourPrice() == null) continue;
            if (!isInQuarter(p.getEndDate(), year, quarter)) continue;

            double estHours = projectRepository.findTotalEstimatedHoursByProjectId(p.getProjectId());
            estimated = estimated.add(p.getHourPrice().multiply(BigDecimal.valueOf(estHours)));
        }

        return estimated;
    }

    // Aktuelle indkomst (kvartal)
    public BigDecimal getQuarterlyActualRevenue(int year, int quarter) {
        List<Project> projects = projectRepository.findAll();
        if (projects == null) return BigDecimal.ZERO;

        BigDecimal actual = BigDecimal.ZERO;

        for (Project p : projects) {
            if (p.getHourPrice() == null) continue;
            if (!isInQuarter(p.getEndDate(), year, quarter)) continue;

            double actHours = projectRepository.findTotalActualHoursByProjectId(p.getProjectId());
            actual = actual.add(p.getHourPrice().multiply(BigDecimal.valueOf(actHours)));
        }

        return actual;
    }

    // Årlig indtjening - alle projekter
    public BigDecimal getAnnualRevenue(int year) {
        List<Project> projects = projectRepository.findAll();
        if (projects == null) return BigDecimal.ZERO;

        return projects.stream()
                .filter(p -> p.getStartDate() != null && p.getStartDate().getYear() == year
                        || p.getEndDate() != null && p.getEndDate().getYear() == year)
                .map(p -> {
                    if (p.getHourPrice() == null) return BigDecimal.ZERO;
                    double hours = projectRepository.findTotalEstimatedHoursByProjectId(p.getProjectId());
                    return p.getHourPrice().multiply(BigDecimal.valueOf(hours));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getActiveProjectCount() {
        return projectRepository.countActiveProjects();
    }

    public int getProjectsNeedingAttentionCount() {
        return projectRepository.countProjectsNeedingAttention();
    }

    public double getTotalEstimatedHoursByProjectId(int projectId) {
        validateId(projectId);
        return projectRepository.findTotalEstimatedHoursByProjectId(projectId);
    }

    public double getTotalActualHoursByProjectId(int projectId) {
        validateId(projectId);
        return projectRepository.findTotalActualHoursByProjectId(projectId);
    }

    public double getRemainingHoursByProjectId(int projectId) {
       validateId(projectId);
        double estimatedHours = projectRepository.findTotalEstimatedHoursByProjectId(projectId);
        double actualHours = projectRepository.findTotalActualHoursByProjectId(projectId);
        return estimatedHours - actualHours;
    }




}
