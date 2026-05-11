package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)

class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository repo;

    //OBS: Opdater, når Employee og Tasks er på plads
    @Test
    void readProject1ByProjectId() {
        Project p1 = repo.findById(1);

        assertThat(p1).isNotNull();
        assertEquals(p1.getTitle(), ("Sanistål Webshop Redesign"));
        assertEquals(p1.getDescription(), "Komplet redesign af B2B webshop.");
        assertEquals(p1.getStartDate(), LocalDate.of(2025, 9, 1));
        assertEquals(p1.getEndDate(), LocalDate.of(2026, 1, 15));
        assertEquals(p1.getBudget(), new BigDecimal("450000.00"));
        // assert Employee
        assertEquals(p1.getParentProjectId(), null);
        // assert subprojects
        // assert tasks

    }

    //OBS: Opdater, når employee og Tasks er på plads
    @Test
    void insertAndReadBack() {
        Project insertProject = new Project(
                null,
                "Test Projekt Management",
                "Udvikling",
                LocalDate.of(2026,1,1),
                LocalDate.of(2026, 12, 1),
                // employee
                new BigDecimal("32000.00"),
                "Not Started",
                null,
                null,
                null
        );

        Project savedProject = repo.insert(insertProject, null);
        Project lookupProject = repo.findById(savedProject.getProjectId());

        assertThat(lookupProject).isNotNull();


    }


}