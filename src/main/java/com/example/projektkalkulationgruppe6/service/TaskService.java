package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.*;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.repository.TaskRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public List<Task> findAllTask() {
        try {
            return taskRepository.findAllTask();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: could not retrieve tasks", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Task> getTreesByProjectId(int projectId) {
        try {
            return taskRepository.findParentsAndChildrenByProjectId(projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException ("Error: Could not retrieve tasks with Project ID " + projectId, ex);
        }
    }


    @Transactional(readOnly = true)
    public Task findById(int id) {
        try {
            Task task = taskRepository.findById(id);
            if (task == null) {
                throw new InvalidTaskException("Task with ID " + id + " was not found");
            }
            return task;
        } catch (DataAccessException ex) {
            throw new InvalidTaskException("Error: Could not retrieve task with ID");
        }
    }

    @Transactional
    public Task create(Task task, int projectId, List<Integer> competenceIds, List<Integer> employeeIds) {
        validateTask(task);
        try {
            Task savedTask = taskRepository.insert(task, projectId);

            if (!competenceIds.isEmpty()){
                for (int competenceId : competenceIds) {
                    taskRepository.addCompetenceToTask(savedTask.getTaskId(), competenceId);
                }
            }

            if (!employeeIds.isEmpty()){
                for (int employeeId : employeeIds) {
                    taskRepository.addAssigneeToTask(savedTask.getTaskId(), employeeId);
                }
            }

            return savedTask;

        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: the task was not created.", ex);
        }
    }

    @Transactional
    public Task update (Task task) {
        validateTask(task);
        if (task.getTaskId() == null){
            throw new InvalidTaskException("Task ID is required for update.");
        }
        validateId(task.getTaskId());
        try {
              boolean updated = taskRepository.update(task);
              if (!updated) {
                  throw new TaskNotFoundException(task.getTaskId());
              } return task;
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: the task was not updated.", ex);
        }
    }

    @Transactional
    public void deleteById(int taskId) {
        validateId(taskId);
        try {
            boolean deleted = taskRepository.deleteById(taskId);
            if (!deleted) {
                throw new TaskNotFoundException(taskId);
            }
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: Cannot delete task.", ex);
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new InvalidTaskException("ID must be a positive number");
        }
    }

    private void validateTask(Task task) {

        if (task == null) {
            throw new InvalidTaskException("Task is required");
        }

        String title = task.getTitle();
        if (title == null || title.isBlank()) {
            throw new InvalidTaskException("Title is required");
        }

        if (task.getStartDate() != null && task.getEndDate() != null) {
            if (task.getStartDate().isAfter(task.getEndDate())) {
                throw new InvalidTaskException("Start date cannot be after end date");
            }
        }

        if (task.getStartDate() == null) {
            throw new InvalidTaskException("Start date is required.");
        }

        if (task.getEndDate() == null) {
            throw new InvalidTaskException("End date is required.");
        }

        if (task.getEstimatedHours() < 0) {
            throw new InvalidTaskException("Estimated hours cannot be negative");
        }
        if (task.getActualHours() < 0) {
            throw new InvalidTaskException("Actual hours cannot be negative");
        }

        if (title.length() > 50) {
            throw new InvalidTaskException("Title cannot exceed 50 characters.");
        }

    }

    public void addAssigneeToTask(int taskId, int employeeId) {
        try {
            taskRepository.addAssigneeToTask(taskId, employeeId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: could not add assignee to task", ex);
        }
    }

    public void removeAssigneeFromTask(int taskId, int employeeId) {
        try {
            taskRepository.removeAssigneeFromTask(taskId, employeeId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: could not remove assignee from task", ex);
        }
    }

    public List<TaskRepository.CompetenceOption> findAllCompetencies() {
        return taskRepository.findAllCompetencies();
    }

    public void addCompetenceToTask(int taskId, int competenceId) {
        try {
            taskRepository.addCompetenceToTask(taskId, competenceId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: could not add competence to task", ex);
        }
    }

    public void removeCompetenceFromTask(int taskId, int competenceId) {
        try {
            taskRepository.removeCompetenceFromTask(taskId, competenceId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: could not remove competence from task", ex);
        }
    }

    public int getCountOfAllTasksDueThisWeek(){
        return taskRepository.findAllTasksDueThisWeek().size();
    }

    //Tag stilling til om denne skaber noget værdi
    public String getDeadlineStatus(int id) {

        Task task = taskRepository.findById(id);

        if (task == null) {
            throw new TaskNotFoundException(id);
        }

        if (task.getEndDate() == null) {
            throw new InvalidTaskException("Project has no deadline.");
        }

        LocalDate today = LocalDate.now();
        long daysRemaining =
                ChronoUnit.DAYS.between(today, task.getEndDate());

        if (daysRemaining < 0) {
            return "Behind";
        }

        if (daysRemaining <= 2) {
            return "Needs attention";
        }

        return "On time";
    }

    public int getAllTaskCount() {
        return taskRepository.countAllTasks();
    }

    public int getTaskCountByProjectId(int projectId) {
        return taskRepository.countTasksByProjectId(projectId);
    }

    public int getCompletedTaskCountByProjectId(int projectId) {
        return taskRepository.countCompletedTasksByProjectId(projectId);
    }

    public BigDecimal getEstimatedHoursThisWeek() {
        return taskRepository.sumEstimatedHoursThisWeek();
    }



}


