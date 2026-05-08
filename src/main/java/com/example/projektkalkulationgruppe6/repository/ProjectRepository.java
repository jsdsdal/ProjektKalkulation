package com.example.projektkalkulationgruppe6.repository;

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
                rs.getBigDecimal("budget"),
                rs.getString("status_title"),
                rs.getInt("parent_project_id"),
                null, // subProjects sættes senere
                null // tasks sættes senere
        );
    };

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Project findById(int id) {
        String sql = """
                SELECT * FROM project
                WHERE project_id = ?
                """;
        try {
            Project p = jdbcTemplate.queryForObject(sql, projectRowMapper, id);
            //attach subProjects
            return p;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Project> findParentAndChildrenById(int id) {
        return null;
    }

    public Project insert(Project project, Integer parentId) {
        String sql = """
                INSERT INTO project (
                    title, description, start_date, end_date, 
                    budget, employee_id, parent_project_id, status_id
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
            ps.setBigDecimal(5, project.getBudget());
            ps.setInt(6, 0); // OBS HUSK OG RET TIL SENERE
            ps.setInt(7, parentId);
            ps.setInt(8, 0); // OBS HUSK AT RETTE SENERE
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
