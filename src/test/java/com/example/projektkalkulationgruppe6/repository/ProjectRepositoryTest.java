package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)

class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository repo;

    //OBS: Opdater, når Tasks er på plads
    @Test
    void readProject1ByProjectId() {
        Project p1 = repo.findById(1);

        assertThat(p1).isNotNull();
        assertEquals(p1.getTitle(), ("Sanistål Webshop Redesign"));
        assertEquals(p1.getDescription(), "Komplet redesign af B2B webshop.");
        assertEquals(p1.getStartDate(), LocalDate.of(2025, 9, 1));
        assertEquals(p1.getEndDate(), LocalDate.of(2026, 1, 15));
        assertEquals(p1.getHourPrice(), new BigDecimal("1000.00"));

        assertEquals(p1.getParentProjectId(), null);
        // assert subprojects
        // assert tasks

    }

    @Test
    void readProjectShouldReturnNull() {
        assertNull(repo.findById(4));
    }

    //OBS: Opdater, når employee og Tasks er på plads
//    @Test
//    void insertAndReadBack() {
//        Project insertProject = new Project(
//                null,
//                "Test Projekt Management",
//                "Udvikling",
//                LocalDate.of(2026,1,1),
//                LocalDate.of(2026, 12, 1),
//                new Employee(),
//                new BigDecimal("1250.00"),
//                "Not Started",
//                null,
//                null,
//                null
//        );
//
//        Project savedProject = repo.insert(insertProject);
//        Project lookupProject = repo.findById(savedProject.getProjectId());
//
//        assertThat(lookupProject).isNotNull();
//        assertThat(lookupProject.getProjectId()).isNotNull();
//        assertEquals(100, lookupProject.getProjectId());
//        assertEquals("Test Projekt Management", lookupProject.getTitle());
//        assertEquals("Udvikling", lookupProject.getDescription());
//        assertEquals(LocalDate.of(2026, 1, 1), lookupProject.getStartDate());
//        assertEquals(LocalDate.of(2026, 12, 1), lookupProject.getEndDate());
//        assertEquals(new BigDecimal("1250.00"), lookupProject.getHourPrice());
//        assertEquals("Not Started", lookupProject.getStatus());
//        assertNull(lookupProject.getParentProjectId());
//        assertNull(lookupProject.getSubProjects());
//        assertNull(lookupProject.getTasks());
//
//    }

    @Test
    void ShouldGetStatusList() {
        List<String> list = repo.findAllStatusList();

        assertThat(list).isNotEmpty();
        assertEquals("Not Started", list.get(0));
        assertEquals("Estimate in Progress", list.get(1));
        assertEquals("Estimate Done", list.get(2));
        assertEquals("Actual Hours Done", list.get(3));
        assertEquals("Rejected", list.get(4));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));

    }

    @Test
    void ShouldReturnProjectAndSubProject() {
        List<Project> list = repo.findParentAndChildrenById(2);
        assertThat(list).isNotEmpty();
        assertEquals(2, list.size());
    }

    @Test
    void ShouldReturnProjectOnly() {
        List<Project> list = repo.findParentAndChildrenById(1);
        assertThat(list).isNotEmpty();
        assertEquals(1, list.size());
    }


    @Test
    void ShouldReturnEmptyList() {
        List<Project> list = repo.findParentAndChildrenById(3);
        assertThat(list).isEmpty();

    }

    @Test
    void DeleteByIdExistingProjectReturnsTrue(){
        boolean result = repo.deleteById(1);
        assertTrue(result);
    }

    @Test
    void DeleteByIdNonExistingProjectReturnsTrue(){
        boolean result = repo.deleteById(9999);
        assertFalse(result);
    }






}