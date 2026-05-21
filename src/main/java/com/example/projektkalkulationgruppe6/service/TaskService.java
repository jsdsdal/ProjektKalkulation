package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
import com.example.projektkalkulationgruppe6.exception.TaskNotFoundException;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.repository.TaskRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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

    public List<Task> getTreesByProjectId(int projectId) {
        try {
            return taskRepository.findParentsAndChildrenByProjectId(projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException ("Error: Could not retrieve tasks with Project ID " + projectId, ex);
        }
    }



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

    public Task create(Task task, int projectId, List<Integer> competenceIds, List<Integer> employeeIds) {
        validateTask(task);
        try {
            Task savedTask = taskRepository.insert(task, projectId);

            if (competenceIds !=null){
                for (int competenceId : competenceIds) {
                    taskRepository.addCompetenceToTask(savedTask.getTaskId(), competenceId);
                }
            }

            if (employeeIds !=null){
                for (int employeeId : employeeIds) {
                    taskRepository.addAssigneeToTask(savedTask.getTaskId(), employeeId);
                }
            }

            return savedTask;

        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: the task was not created.", ex);
        }
    }

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



}


