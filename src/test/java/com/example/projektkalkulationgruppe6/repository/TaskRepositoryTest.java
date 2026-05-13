package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void readTask1ByTaskId() {
        Task t1 = repo.findById(1);


        assertThat(t1).isNotNull();
        assertEquals(t1.getTitle(), ("UX Research"));
        assertEquals(t1.getDescription(), ("Brugerinterviews og wireframes"));
        assertEquals(t1.getStartDate(), LocalDate.of(2025, 9, 1));
        assertEquals(t1.getEndDate(), LocalDate.of(2025, 9, 30));
        assertEquals(t1.getEstimatedHours(), 24.00);
        assertEquals(t1.getActualHours(), 28.00);
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
            assertThat(lookupTask.getSubTask()).isEqualTo(savedTask.getSubTask());
            assertThat(lookupTask.getAsignees()).isEqualTo(savedTask.getAsignees());
            assertThat(lookupTask.getCompetencies()).isEqualTo(savedTask.getCompetencies());
    }

    @Test
    void testFindParentsAndChildrenByProjectId() {
        List<Task> tasks = repo.findParentsAndChildrenByProjectId(1);

        assertThat(tasks).isNotNull();
        assertThat(tasks).hasSize(3);

        Task first = tasks.get(0);
        assertEquals("UX Research", first.getTitle());
        assertEquals(LocalDate.of(2025, 9, 1), first.getStartDate());
        assertEquals(LocalDate.of(2025, 9, 30), first.getEndDate());
        assertEquals(24.00, first.getEstimatedHours());
        assertEquals(28.00, first.getActualHours());
        assertNull(first.getParentTaskId());
    }

}