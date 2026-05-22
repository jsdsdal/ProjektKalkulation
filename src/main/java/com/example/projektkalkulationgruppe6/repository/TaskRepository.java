package com.example.projektkalkulationgruppe6.repository;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Task> taskRowMapper = (rs, rowNum) -> new Task(
            rs.getInt("task_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getDouble("estimated_hours"),
            rs.getDouble("actual_hours"),
            (Integer) rs.getObject("project_id"),
            (Integer) rs.getObject("parent_task_id"),
            null, // subTasks
            null, // assignees
            null // competencies
    );

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record CompetenceOption(int id, String title){}

    public List<Task> findAllTask() {
        String sql = """
                SELECT task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id
                FROM task
                ORDER BY task_id
                """;
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    public Task findById(Integer id) {
        String sql = """
                SELECT *
                FROM task
                WHERE task_id = ?
                """;
        Task task = jdbcTemplate.queryForObject(sql, taskRowMapper, id);
        attachCompetencies(List.of(task));
        attachAssignees(List.of(task));

        String subTaskSql = """
                SELECT *
                FROM task
                WHERE parent_task_id = ?
        """;
        List<Task> subTasks = jdbcTemplate.query(subTaskSql, taskRowMapper, id);
        task.setSubTask(subTasks);

        return task;
    }

    public List<Task> findParentsAndChildrenByProjectId(int projectId) {
        String sql = """
                SELECT *
                FROM task
                WHERE project_id = ?
                """;
        List<Task> tasks = jdbcTemplate.query(sql, taskRowMapper, projectId);
        attachCompetencies(tasks);
        attachAssignees(tasks);
        attachSubTasks(tasks);

        return tasks.stream()
                .filter(t -> t.getParentTaskId() == null)
                .collect(Collectors.toList());
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

        Number key = keyHolder.getKey();
        if (key != null) {
            task.setTaskId(key.intValue());
        } else {
            throw new IllegalStateException("Error: Could not retrieve generated TaskID.");
        }

        return task;

    }

    public boolean update(Task task) {
        String sql = """
                UPDATE task
                SET title = ?, description = ?, start_date = ?, end_date = ?, estimated_hours = ?, actual_hours = ?
                WHERE task_id = ?
                """;
        int rowsUpdated = jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                java.sql.Date.valueOf(task.getStartDate()),
                java.sql.Date.valueOf(task.getEndDate()),
                task.getEstimatedHours(),
                task.getActualHours(),
                task.getTaskId());
        return rowsUpdated > 0;

    }

    public boolean deleteById(int taskId){
        String sql = """
        DELETE FROM task
        WHERE task_id = ?
        """;

        int rowDeleted = jdbcTemplate.update(sql, taskId);
        return rowDeleted > 0;
    }

    //Assignees

    private List<Employee> findAssigneesByTaskId(int taskId) {
        String sql = """
            SELECT e.employee_id, e.name, e.email, e.password
                FROM task_employee te
                JOIN employee e
                ON te.employee_id = e.employee_id
                WHERE te.task_id = ?
                ORDER BY te.employee_id
            """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Employee(
                rs.getInt("employee_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                null
        ), taskId);
    }

    private void attachAssignees(List<Task> tasks) {
        for (Task t : tasks) {
            t.setAssignees(findAssigneesByTaskId(t.getTaskId()));
        }
    }

    public void addAssigneeToTask(int taskId, int employeeId) {
        String sql = """
        INSERT INTO task_employee (task_id, employee_id)
        VALUES (?, ?)
        """;
        jdbcTemplate.update(sql, taskId, employeeId);
    }


    public void removeAssigneeFromTask(int taskId, int employeeId) {
        String sql = """
        DELETE FROM task_employee
        WHERE task_id = ?
        AND employee_id = ?
        """;
        jdbcTemplate.update(sql, taskId, employeeId);
    }

    //Competencies

    public List<CompetenceOption> findAllCompetencies() {
        String sql = """
        SELECT competence_id, title
        FROM competence
        ORDER BY competence_id
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CompetenceOption(rs.getInt("competence_id"), rs.getString("title")));
    }
    private List<String> findCompetenciesByTaskId(int taskId) {
        String sql = """
            SELECT c.title
                FROM task_competence tc
                JOIN competence c
                ON tc.competence_id = c.competence_id
                WHERE tc.task_id = ?
                ORDER BY tc.competence_id
            """;
        return jdbcTemplate.queryForList(sql, String.class, taskId);
    }

    private void attachCompetencies(List<Task> tasks) {
        for (Task t : tasks) {
            List<String> competencies = findCompetenciesByTaskId(t.getTaskId());
            t.setCompetencies(competencies);
        }
    }

    public void addCompetenceToTask(int taskId, int competenceId) {
        String sql = """
        INSERT INTO task_competence (task_id, competence_id)
        VALUES (?, ?)
        """;
        jdbcTemplate.update(sql, taskId, competenceId);
    }


    public void removeCompetenceFromTask(int taskId, int competenceId) {
        String sql = """
        DELETE FROM task_competence
        WHERE task_id = ? 
        AND competence_id = ?
        """;
        jdbcTemplate.update(sql, taskId, competenceId); }


//SubTask

    private void attachSubTasks(List<Task> tasks) {
        for (Task t : tasks) {
            List<Task> subTasks = tasks.stream()
                    .filter(sub -> t.getTaskId().equals(sub.getParentTaskId()))
                    .collect(Collectors.toList());
            t.setSubTask(subTasks);
        }
    }


    public List<Task> findAllTasksDueThisWeek() {
        String sql = """
            SELECT task_id, title, description,
                   start_date, end_date,
                   estimated_hours, actual_hours,
                   project_id, parent_task_id
            FROM task
            WHERE end_date BETWEEN
                DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
                AND
                DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 6 DAY)
            ORDER BY end_date ASC
            """;
        return jdbcTemplate.query(sql, taskRowMapper);
    }


}

