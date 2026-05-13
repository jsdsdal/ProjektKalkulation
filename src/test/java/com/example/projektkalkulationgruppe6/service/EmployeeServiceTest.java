package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.InvalidEmployeeException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    // Hjælpemetode til at bygge en standard Employee
    private Employee buildEmployee(String email, String password) {
        return new Employee(1, "Test Bruger", email, password,
                List.of("Java", "SQL"));
    }

    //LOGIN MED KORREKTE CREDENTIALS

    @Test
    void loginWithValidEmailAndPasswordReturnsEmployee(){
        Employee employee = buildEmployee("test@alphasolutions.com", "test123");
        when(employeeRepository.findByEmail("test@alphasolutions.com")).thenReturn(employee);

        Employee result = employeeService.login("test@alphasolutions.com", "test123");

        assertEquals(employee, result);

    }

    @Test
    void loginReturnsCorrectEmployeeName() {
        Employee employee = buildEmployee("test@example.com", "secret123");
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(employee);

        Employee result = employeeService.login("test@example.com", "secret123");

        assertEquals("Test Bruger", result.getName());
    }

    @Test
    void LoginReturnsEmployeeHasCompetencies(){
        Employee employee = buildEmployee("test@example.com", "secret123");
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(employee);

        Employee result = employeeService.login("test@example.com", "secret123");

        assertFalse(result.getCompetencies().isEmpty());
    }


    //LOGIN MED FORKERTE CREDENTIALS

    @Test
    void loginWrongPasswordThrowsInvalidEmployeeException() {
        Employee employee = buildEmployee("test@example.com", "test123");
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(employee);

        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("test@example.com", "forkert123"));
    }

    @Test
    void loginNonExistingEmailThrowsInvalidEmployeeException() {
        when(employeeRepository.findByEmail("ukendt@example.com")).thenReturn(null);

        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("ukendt@example.com", "password"));
    }

    @Test
    void loginCallsRepositoryOneTime() {
        when(employeeRepository.findByEmail(any())).thenReturn(null);

        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("test@example.com", "password"));

        verify(employeeRepository, times(1)).findByEmail("test@example.com");
    }


    //VALIDATE_EMAIL - UGYLDIG EMAIL

    @Test
    void loginWithNullEmailThrowsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login(null, "password"));
    }

    @Test
    void loginWithEmptyEmailThrowsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("", "password"));
    }

    @Test
    void loginWithBlankEmailThrowsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("   ", "password"));
    }

    @Test
    void loginWithEmailOver100ThrowsInvalidEmployeeException() {
        // 95 a'er + "@b.dk" = 101 tegn
        String langEmail = "a".repeat(95) + "@b.dk";
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login(langEmail, "password"));
    }

    @Test
    void loginWithEmailWithoutAtSignInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("ingensnabel.dk", "password"));
    }

    @Test
    void loginWithEmailWithoutDomainThrowsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("test@", "password"));
    }

    @Test
    void loginWithEmailWithoutTLDThrowsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("test@domain", "password"));
    }

    @Test
    void loginWithEmailWithOneSignInTLDThrowsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login("test@domain.d", "password"));
    }

    //REPOSITORY KALD - UGYLDIG EMAIL

    @Test
    void loginWithNullEmailRepositoryNotCalled() {
        assertThrows(InvalidEmployeeException.class,
                () -> employeeService.login(null, "password"));

        verifyNoInteractions(employeeRepository);
    }




}