package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.exception.ProjectNotFoundException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks ProjectService projectService;


    Project mainTestProject = new Project(
            null,
            "Test Projekt Management",
            "Udvikling",
            LocalDate.of(2026,1,1),
            LocalDate.of(2026, 12, 1),
            new Employee(1, "test", "email", "password", new ArrayList<String>()),
            new BigDecimal("1250.00"),
            "Not Started",
            null,
            null,
            null
    );

    @Test
    void ThrowDatabaseOperationExceptionDueToFindAllDataAccessException() {
        when(projectRepository.findAll()).thenThrow(new EmptyResultDataAccessException(10));
        assertThrows(DatabaseOperationException.class, () -> projectService.findAll());
    }

    @Test
    void ThrowInvalidProjectExceptionDueToEmptyProject() {
        Project p = new Project();
        assertThrows(InvalidProjectException.class, () -> projectService.create(p, 1));
    }



    @Test
    void ThrowInvalidProjectExceptionDueToEmptyProjectTitle() {
        Project p = mainTestProject;
        assertDoesNotThrow(() -> projectService.create(p, 1));

        p.setTitle("");
        assertThrows(InvalidProjectException.class, () -> projectService.create(p, 1));
    }

    @Test
    void ThrowInvalidProjectExceptionDueToEmptyProjectStatus() {
        Project p = mainTestProject;
        when(employeeService.findById(1)).thenReturn(
                new Employee(1, "Lars", "test@test.dk", "1234", List.of("sql"))
        );
        assertDoesNotThrow(() -> projectService.create(p, 1));

        p.setStatus("");
        assertThrows(InvalidProjectException.class, () -> projectService.create(p, 1));
    }

    @Test
    void ThrowInvalidProjectExceptionDueToEmptyProjectOwner() {
        Project p = mainTestProject;
        when(employeeService.findById(1)).thenReturn(
                new Employee(1, "Test Bruger", "email", "password",
                        List.of("Java", "SQL")));
        assertDoesNotThrow(() -> projectService.create(p, 1));

        assertThrows(InvalidProjectException.class, () -> projectService.create(p,null));
    }

    @Test
    void ThrowDatabaseOperationExceptionOnDataAccessException() {
        Project p = mainTestProject;
        when(projectRepository.insert(p)).thenThrow(new EmptyResultDataAccessException(10));
        assertThrows(DatabaseOperationException.class, () -> projectService.create(p, 1));
    }



    //DELETE

    @Test
    void deleteWithValidIdCallsRepository() {
        when(projectRepository.deleteById(1)).thenReturn(true);

        projectService.delete(1);

        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteProjectNotFoundThrowsProjectNotFoundException() {
        when(projectRepository.deleteById(9999)).thenReturn(false);

        assertThrows(ProjectNotFoundException.class,
                () -> projectService.delete(9999));
    }

    @Test
    void deleteWithInvalidIdThrowsInvalidProjectException() {
        assertThrows(InvalidProjectException.class,
                () -> projectService.delete(0));
    }

    @Test
    void deleteWithNegativeIdThrowsInvalidProjectException() {
        assertThrows(InvalidProjectException.class,
                () -> projectService.delete(-1));
    }

    @Test
    void deleteWithInvalidIdDoesNotCallRepository() {
        assertThrows(InvalidProjectException.class,
                () -> projectService.delete(0));

        verifyNoInteractions(projectRepository);
    }


}