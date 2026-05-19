package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    TaskService taskService;

    Task mainTestTask = new Task(
            null,
            "Test Task Title",
            "Test Description",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 12),
            11.00,
            20.00,
            null,
            null,
            null,
            null

    );

    @BeforeEach
    void setUp() {
        mainTestTask.setTitle("Test Task Title");
        mainTestTask.setStartDate(LocalDate.of(2026, 1, 1));
        mainTestTask.setEndDate(LocalDate.of(2026, 12, 12));
    }

    @Test
    void ThrowDatabaseOperationExceptionWhenFindAllFails() {
        when(taskRepository.findAllTask())
                .thenThrow(new EmptyResultDataAccessException(1));
        assertThrows(DatabaseOperationException.class, () -> taskService.findAllTask());
    }

    @Test
    void ThrowInvalidTaskExceptionDueToEmptyProject() {
        Task t = new Task();
        assertThrows(InvalidTaskException.class, () -> taskService.create(t, 1));
    }

    @Test
    void ThrowInvalidTaskExceptionDueToEmptyTaskTitle() {
        mainTestTask.setTitle("");
        assertThrows(InvalidTaskException.class, () -> taskService.create(mainTestTask, 1));
    }

    @Test
    void ThrowInvalidTaskExceptionDueToNullTaskTitle() {
        mainTestTask.setTitle(null);
        assertThrows(InvalidTaskException.class, () -> taskService.create(mainTestTask, 1));
    }

    @Test
    void ThrowDatabaseOperationExceptionOnDataAccessException() {
        Task t = mainTestTask;
        when(taskRepository.insert(t, 1)).thenThrow(new EmptyResultDataAccessException(10));
        assertThrows(DatabaseOperationException.class, () -> taskService.create(t, 1));
    }


    @Test
    void ThrowInvalidTaskExceptionWhenStarDateIsAfterEndDate() {
        mainTestTask.setStartDate(LocalDate.of(2026, 12, 12));
        mainTestTask.setEndDate(LocalDate.of(2026, 1, 1));
        assertThrows(InvalidTaskException.class, () -> taskService.create(mainTestTask, 1));
    }

    @Test
    void deleteByIdShouldDeleteTask() {
        when(taskRepository.deleteById(1)).thenReturn(true);
        assertDoesNotThrow(() -> taskService.deleteById(1));
    }

    @Test
    void ThrowDatabaseOperationExceptionWhenDeleteFails() {
        when(taskRepository.deleteById(1))
                .thenThrow(new EmptyResultDataAccessException(1));
        assertThrows(DatabaseOperationException.class, () -> taskService.deleteById(1));
    }


    @Test
    void ThrowDatabaseOperationExceptionWhenAddAssigneeToTaskFails() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(taskRepository).addAssigneeToTask(1, 1);
        assertThrows(DatabaseOperationException.class,
                () -> taskService.addAssigneeToTask(1, 1));
    }

    @Test
    void ThrowDatabaseOperationExceptionWhenAddCompetenceToTaskFails() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(taskRepository).addCompetenceToTask(1, 1);
        assertThrows(DatabaseOperationException.class,
                () -> taskService.addCompetenceToTask(1, 1));
    }
}