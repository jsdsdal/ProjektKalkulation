package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.model.Project;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ProjectRepository {
    private final JdbcTemplate jdbcTemplate;
    private RowMapper<Project> projectRowMapper = (rs, rowNum) -> {
        java.sql.Date sqlStartDate = rs.getDate("start_date");
        java.sql.Date sqlEndDate = rs.getDate("end_date");

        return new Project(
                rs.getInt("project_id"),
                rs.getString("title"),
                rs.getString("description"),
                sqlStartDate != null ? sqlStartDate.toLocalDate() : null,
                sqlEndDate != null ? sqlEndDate.toLocalDate() : null,
                null, // employee sættes bagefter
                rs.getBigDecimal("hour_price"),
                rs.getString("status_title"),
                (Integer) rs.getObject("parent_project_id"),
                null, // subProjects sættes senere
                null // tasks sættes senere
        );
    };

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Project findById(int id) {
        String sql = """
            SELECT p.*, s.title AS status_title
            FROM project p
            JOIN status s ON p.status_id = s.status_id
            WHERE p.project_id = ?
                """;
        try {
            Project p = jdbcTemplate.queryForObject(sql, projectRowMapper, id);
            //attach subProjects
            return p;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Project> findSubprojectsByParentId(int parentId) {
        String sql = """
                SELECT p.*, s.title AS status_title
                FROM project p
                JOIN status s ON p.status_id = s.status_id
                WHERE p.parent_project_id = ?
                """;
        return jdbcTemplate.query(sql, projectRowMapper, parentId);
    }

    public Project findByIdWithSubprojects(int id) {
        Project parent = findById(id);
        if (parent == null) return null;

        List<Project> subprojects = findSubprojectsByParentId(id);
        parent.setSubProjects(subprojects);
        return parent;
    }

    public List<Project> findParentAndChildrenById(int id) {
        String sql = """
        WITH RECURSIVE project_tree(
            project_id, title, description, start_date, end_date,
            hour_price, employee_id, parent_project_id, status_id,
            status_title, depth
        ) AS (
            SELECT p.*, s.title AS status_title, 0 AS depth
            FROM project p
            JOIN status s ON p.status_id = s.status_id
            WHERE parent_project_id IS NULL AND project_id = ?

            UNION ALL

            SELECT p.*, s.title AS status_title, pt.depth + 1
            FROM project p
            JOIN status s ON p.status_id = s.status_id
            JOIN project_tree pt ON p.parent_project_id = pt.project_id
        )
        SELECT * FROM project_tree
                """;
        return jdbcTemplate.query(sql, projectRowMapper, id);

    }

    public int findStatusIdByTitle(String title) {
        String sql = """
                SELECT status_id FROM status WHERE title = ?
                """;
        try {
            int id = jdbcTemplate.queryForObject(sql, Integer.class, title);
            return id;
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidProjectException("Ugyldig status: " + title);
        }
    }

    public List<String> findAllStatusList() {
        String sql = """
                SELECT title 
                FROM status
                ORDER BY status_id
                """;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public Project insert(Project project) {
        String sql = """
                INSERT INTO project (
                    title, description, start_date, end_date, 
                    hour_price, employee_id, parent_project_id, status_id
                    )
                    values(?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setDate(3, project.getStartDate() != null ? java.sql.Date.valueOf(project.getStartDate()) : null);
            ps.setDate(4, project.getEndDate() != null ? java.sql.Date.valueOf(project.getEndDate()) : null);
            ps.setBigDecimal(5, project.getHourPrice());
            ps.setInt(6, project.getProjectOwner().getEmployeeId());
            ps.setObject(7, project.getParentProjectId());
            ps.setInt(8, findStatusIdByTitle(project.getStatus()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated id.");
        }

        // insert subProjects

        return findById(key.intValue());

    }








}
