package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

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