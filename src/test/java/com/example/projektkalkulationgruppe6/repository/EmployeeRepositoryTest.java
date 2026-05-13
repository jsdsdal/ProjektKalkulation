package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")

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