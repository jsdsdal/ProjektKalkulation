package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)

class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository repo;

    @Test
    void readProject1ByProjectId() {
        Project p1 = repo.findById(1);

        ass

        assertThat(p1).isNotNull();
    }
}