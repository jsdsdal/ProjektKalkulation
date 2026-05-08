package com.example.projektkalkulationgruppe6.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Project {
    private Integer projectId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Employee projectOwner;
    private BigDecimal budget;
    private String status;
    private Integer parentProjectId;
    private List<Project> subProjects;
    private List<Task> tasks;


    public Project() {
        // tom Konstruktor
    }
    public Project (Integer projectId, String title, String description,
                    LocalDate startDate, LocalDate endDate, Employee projectOwner,
                    BigDecimal budget, String status, Integer parentProjectId,
                    List<Project> subProjects, List<Task> tasks) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectOwner = projectOwner;
        this.budget = budget;
        this.status = status;
        this.parentProjectId = parentProjectId;
        this.subProjects = subProjects;
        this.tasks = tasks;
    }

    public Integer getProjectId() { return projectId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return  endDate; }
    public Employee getProjectOwner() { return projectOwner; }
    public BigDecimal getBudget() { return budget; }
    public String getStatus() { return status; }
    public Integer getParentProjectId() { return parentProjectId; }
    public List<Project> getSubProjects() { return subProjects; }
    public List<Task> getTasks() { return tasks; }

    public String toString() {
        return "ProjectID: " + projectId + ", Title: " + title + ", Description: " + description +
                ", StartDate: " + startDate + ", EndDate: " + endDate + ", ProjectOwner: " + projectOwner +
                ", Budget: " + budget + ", Status: " + status + " ParentProjectID: " + parentProjectId +
                ", SubProjects: " + subProjects + ", Tasks: " + tasks;
    }




}
