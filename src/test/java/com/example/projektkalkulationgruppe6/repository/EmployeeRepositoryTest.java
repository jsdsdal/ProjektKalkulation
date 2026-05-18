package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)

class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void shouldReturnAllEmployees() {
        List<Employee> employeeList = repository.findAll();
        Employee e1 = new Employee(5, "Frederik Møller", "frederik@alphasolutions.com", "frederik1234",
                List.of());
        Employee e3 = new Employee(1, "Lars Nielsen", "lars@alphasolutions.com", "lars1234",
                List.of("Project Management", "C#", "SQL"));


        assertThat(employeeList).isNotNull();
        assertEquals(5, employeeList.size());
        assertEquals(e1.getEmployeeId(), employeeList.getFirst().getEmployeeId());
        assertEquals(e1.getName(), employeeList.getFirst().getName());
        assertEquals(e1.getEmail(), employeeList.getFirst().getEmail());
        assertEquals(e1.getPassword(), employeeList.getFirst().getPassword());
        assertEquals(e1.getCompetencies(), employeeList.getFirst().getCompetencies());

        assertEquals(e3.getEmployeeId(), employeeList.get(2).getEmployeeId());
        assertEquals(e3.getName(), employeeList.get(2).getName());
        assertEquals(e3.getEmail(), employeeList.get(2).getEmail());
        assertEquals(e3.getPassword(), employeeList.get(2).getPassword());
        assertEquals(e3.getCompetencies(), employeeList.get(2).getCompetencies());

    }

    @Test
    void testFindByIdShouldReturnEmployee1() {
        Employee result = repository.findById(1);

        assertThat(result).isNotNull();
        assertEquals("Lars Nielsen", result.getName());
        assertEquals("lars@alphasolutions.com", result.getEmail());
    }


    @Test
    void testFindByIdShouldReturnNull() {
        Employee result = repository.findById(6);

        assertEquals(null, result);
    }

    @Test
    void testFindByEmailReturnsEmployee(){
        Employee example = repository.findByEmail("lars@alphasolutions.com");

        assertThat(example).isNotNull();
        assertEquals("Lars Nielsen", example.getName());
        assertEquals("lars@alphasolutions.com", example.getEmail());
    }

    @Test
    void testFindByEmailReturnsNotNullWhenNotFound(){
        Employee example = repository.findByEmail("ukendt@email.com");
        assertNull(example);
    }

    @Test
    void testFindByEmailAttachesCompetencies(){
        Employee example = repository.findByEmail("lars@alphasolutions.com");

        assertNotNull(example);
        assertFalse(example.getCompetencies().isEmpty());
        assertTrue(example.getCompetencies().contains("SQL"));
    }


    @Test
    void testFindByEmailReturnsEmptyCompetenciesWhenNoCompetencies(){
        Employee example = repository.findByEmail("frederik@alphasolutions.com");

        assertNotNull(example);
        assertTrue(example.getCompetencies().isEmpty());
    }







}