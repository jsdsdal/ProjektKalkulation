package com.example.projektkalkulationgruppe6.model;

import java.time.LocalDate;
import java.util.List;

public class Task {
    private Integer taskId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private double estimatedHours;
    private double actualHours;
    private Integer parentTaskId;
    private List<Task> subTask;
    private List<Employee> asignees;
    private List<String> competencies;

    public Task(Integer taskId, String title, String description, LocalDate startDate, LocalDate endDate,
                double estimatedHours, double actualHours, Integer parentTaskId, List<Task> subTask,
                List<Employee> asignees, List<String> competencies) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedHours = estimatedHours;
        this.actualHours = actualHours;
        this.parentTaskId = parentTaskId;
        this.subTask = subTask;
        this.asignees = asignees;
        this.competencies = competencies;
    }

    public Integer getTaskId() {return taskId;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public LocalDate getStartDate() {return startDate;}
    public LocalDate getEndDate() {return endDate;}
    public double getEstimatedHours() {return estimatedHours;}
    public double getActualHours() {return actualHours;}
    public Integer getParentTaskId() {return parentTaskId;}
    public List<Task> getSubTask() {return subTask;}
    public List<Employee> getAsignees() {return asignees;}
    public List<String> getCompetencies() {return competencies;}


    public void setTaskId(Integer taskId) {this.taskId = taskId;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}
    public void setEstimatedHours(double estimatedHours) {this.estimatedHours = estimatedHours;}
    public void setActualHours(double actualHours) {this.actualHours = actualHours;}
    public void setParentTaskId(Integer parentTaskId) {this.parentTaskId = parentTaskId;}
    public void setSubTask(List<Task> subTask) {this.subTask = subTask;}
    public void setAsignees(List<Employee> asignees) {this.asignees = asignees;}
    public void setCompetencies(List<String> competencies) {this.competencies = competencies;}

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", estimatedHours=" + estimatedHours +
                ", actualHours=" + actualHours +
                ", parentTaskId=" + parentTaskId +
                ", subTask=" + subTask +
                ", asignees=" + asignees +
                ", competencies=" + competencies +
                '}';
    }
}
