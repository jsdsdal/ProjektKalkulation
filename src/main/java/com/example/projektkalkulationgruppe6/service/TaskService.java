package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
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

    public List<String> findAllCompetencies() {
        return taskRepository.findAllCompetencies();
    }

    public Task findById(int id) {
        try{
            Task task = taskRepository.findById(id);
            if (task == null) {
                throw new InvalidTaskException("Task with ID " + id + " was not found");
            }
            return task;
        } catch (DataAccessException ex) {
            throw new InvalidTaskException("Error: Could not retrieve task with ID");
        }
    }

    public Task create(Task task, int projectId) {
        validateTask(task);

        try{
            return taskRepository.insert(task, projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: the task was not created.", ex);
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

        }



    public void addAssigneeToTask(int taskId, int employeeId) {
        try {
            taskRepository.addAssigneeToTask(taskId, employeeId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: could not add assignee to task", ex);
        }
    }

    //removeAssigneeFromTask

    public void addCompetenceToTask(int taskId, int competenceId) {
        try {
            taskRepository.addCompetenceToTask(taskId, competenceId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: could not add competence to task", ex);
        }
    }

    //removeCompetenceFromTask
}

