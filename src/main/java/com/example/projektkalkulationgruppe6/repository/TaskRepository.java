package com.example.projektkalkulationgruppe6.repository;
import com.example.projektkalkulationgruppe6.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Task> taskRowMapper = (rs, rowNum) -> {
        //java.sql.Date sqlStartDate = rs.getDate("start_date");
       //java.sql.Date sqlEndDdate = rs.getDate("end_date");

        return new Task(
                rs.getInt("task_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getDouble("estimated_hours"),
                rs.getDouble("actual_hours"),
                (Integer) rs.getObject("parent_task_id"),
                null, // subTasks (fyldes senere via logik)
                null, // assignees (fyldes via attachAssignees)
                null // competencies (fyldes via attachCompetencies)
        );

    };

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Task findById(Integer id) {
        String sql = """
                SELECT *
                FROM task
                WHERE task_id = ?
                """;
        Task task = jdbcTemplate.queryForObject(sql, taskRowMapper, id);

       /* if (task != null) {
            // Husk at hente de tilknyttede lister via dine private hjælpe-metoder
            attachAssignees(Collections.singletonList(task));
            attachCompetencies(Collections.singletonList(task));
        }*/

        return task;
    }

    public List<Task> findParentsAndChildrenByProjectId(int projectId) {
        String sql = """
                SELECT *
                FROM task
                WHERE project_id = ?
                """;
        List<Task> allTask = jdbcTemplate.query(sql, taskRowMapper, projectId);

        return allTask;
    }

    public Task insert(Task task, int projectId) {
        String sql = """
                INSERT INTO task (title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id)
                VALUES (?, ?, ?, ?, ? ,?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(task.getStartDate()));
            ps.setDate(4, java.sql.Date.valueOf(task.getEndDate()));
            ps.setDouble(5, task.getEstimatedHours());
            ps.setDouble(6, task.getActualHours());
            ps.setInt(7, projectId);
            if (task.getParentTaskId() != null) {
                ps.setInt(8, task.getParentTaskId());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }
            return ps;
        }, keyHolder);

        int newId = (int) keyHolder.getKeys().get("task_id");

        Number key = keyHolder.getKey();
        if (key != null) {
            task.setTaskId(key.intValue());
        } else {
            throw new IllegalStateException("Kunne ikke hente genereret task_id.");
        }

        return task;

    }


}

