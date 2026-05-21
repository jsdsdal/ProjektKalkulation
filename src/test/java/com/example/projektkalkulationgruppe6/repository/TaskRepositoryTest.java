package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(statements = "ALTER TABLE task ALTER COLUMN task_id RESTART WITH 100;", executionPhase = BEFORE_TEST_METHOD)

class TaskRepositoryTest {

    @Autowired
    private TaskRepository repo;

    @Test
    void findAllShouldReturnAllTasks() {
        List<Task> tasks = repo.findAllTask();
        assertThat(tasks).isNotNull();
        assertThat(tasks).hasSizeGreaterThan(0);
    }


    @Test
    void readTask1ByTaskId() {
        Task t1 = repo.findById(1);


        assertThat(t1).isNotNull();
        assertEquals(("UX Research"), t1.getTitle());
        assertEquals(("Brugerinterviews og wireframes"), t1.getDescription());
        assertEquals(LocalDate.of(2025, 9, 1), t1.getStartDate());
        assertEquals(LocalDate.of(2025, 9, 30),t1.getEndDate());
        assertEquals(24.00, t1.getEstimatedHours());
        assertEquals(28.00, t1.getActualHours());
        assertNull(t1.getParentTaskId());
    }

    @Test
    void insertAndReadBack() {
        Task insertTask = new Task(
                null,
                "Test Task",
                "Test description",
                LocalDate.of(2026, 10, 4),
                LocalDate.of(2026, 11, 10),
                24.00,
                28.00,
                null,
                null,
                null,
                null,
                null
        );
        Task savedTask = repo.insert(insertTask, 1);
        Task lookupTask = repo.findById(savedTask.getTaskId());

        assertThat(lookupTask).isNotNull();
        assertThat(lookupTask.getTaskId()).isEqualTo(savedTask.getTaskId());
        assertThat(lookupTask.getTitle()).isEqualTo(savedTask.getTitle());
        assertThat(lookupTask.getStartDate()).isEqualTo(insertTask.getStartDate());
        assertThat(lookupTask.getEstimatedHours()).isEqualTo(savedTask.getEstimatedHours());
        assertThat(lookupTask.getActualHours()).isEqualTo(savedTask.getActualHours());
        assertThat(lookupTask.getParentTaskId()).isEqualTo(savedTask.getParentTaskId());
        assertThat(lookupTask.getSubTask()).isEmpty();
        assertThat(lookupTask.getAssignees()).isEmpty();
        assertThat(lookupTask.getCompetencies()).isEmpty();
    }

    @Test
    void deleteByIdShouldDeleteTask() {
        boolean deleted = repo.deleteById(1);
        assertThat(deleted).isTrue();
    }

    @Test
    void deleteByIdShouldReturnFalseWhenTaskDoesNotExist() {
        boolean deleted = repo.deleteById(999);
        assertThat(deleted).isFalse();
    }


    @Test
    void testFindParentsAndChildrenByProjectId() {
        List<Task> tasks = repo.findParentsAndChildrenByProjectId(1);

        assertThat(tasks).isNotNull();
        assertThat(tasks).hasSize(3);

        Task first = tasks.getFirst();
        assertEquals("UX Research", first.getTitle());
        assertEquals(LocalDate.of(2025, 9, 1), first.getStartDate());
        assertEquals(LocalDate.of(2025, 9, 30), first.getEndDate());
        assertEquals(24.00, first.getEstimatedHours());
        assertEquals(28.00, first.getActualHours());
        assertNull(first.getParentTaskId());
    }

    @Test
    void readCompetenciesFromTask1() {
        Task t1 = repo.findById(1);
        assertThat(t1.getCompetencies()).isNotNull();
        assertThat(t1.getCompetencies()).contains("UX/UI Design");
    }

    @Test
    void readAssigneesFromTask1() {
        Task t1 = repo.findById(1);
        assertThat(t1.getAssignees()).isNotNull();
        assertThat(t1.getAssignees()).extracting("name").contains("Sofie Berg");
    }

    @Test
    void readSubTasksFromTask4() {
        Task t4 = repo.findById(4);
        assertThat(t4.getSubTask()).isNotNull();
        assertThat(t4.getSubTask()).hasSize(2);
    }


}