package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Employee;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Employee> employeeRowMapper = (rs, rowNum) ->
            new Employee(
                    rs.getInt("employee_Id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    List.of()
            );


    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAllProjectManagers() {

        return null;
    }

    public List<Employee> findAll() {
        String sql = """
                SELECT employee_id, name, email, password
                FROM employee
                ORDER BY name
                """;
        List<Employee> result = jdbcTemplate.query(sql, employeeRowMapper);

        if (result.isEmpty()) {
            return null;
        }

        attachCompetencies(result);

        return result;
    }

    public Employee findById(int id) {
        String sql = """
                SELECT employee_id, name, email, password
                FROM employee
                WHERE employee_id = ?
                """;

        List<Employee> result = jdbcTemplate.query(sql, employeeRowMapper, id);

        if (result.isEmpty()) {
            return null;
        }
        attachCompetencies(result);
        return result.getFirst();
    }

    public Employee findByEmail(String email){
        String sql = """
                SELECT employee_id, name, email, password
                FROM employee
                WHERE email = ?
                """;

            List<Employee> result = jdbcTemplate.query(sql, employeeRowMapper, email);
            if(result.isEmpty()){
                return null;
            }
            attachCompetencies(result);
            return result.getFirst();
    }

    private List<String> findCompetenciesById(int id) {
        String sql = """
                SELECT c.title
                	FROM employee_competence ec
                    JOIN competence c
                    ON ec.competence_id = c.competence_id
                    WHERE employee_id = ?
                    ORDER BY ec.competence_id
                """;
        return jdbcTemplate.queryForList(sql, String.class, id);
    }

    private void attachCompetencies(List<Employee> employees){
        for(Employee e: employees){
            List<String> competence = findCompetenciesById(e.getEmployeeId());
            e.setCompetencies(competence);
        }

    }

    public BigDecimal sumEstimatedHoursByEmployeeAndMonth(int employeeId, int year, int month) {
        String sql = """
            SELECT COALESCE(SUM(t.estimated_hours), 0)
            FROM task t
            JOIN task_employee te ON t.task_id = te.task_id
            WHERE te.employee_id = ?
            AND MONTH(t.start_date) = ?
            AND YEAR(t.start_date) = ?
            """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, employeeId, month, year);
    }

    public List<Employee> findAllWithTasksCurrentMonth() {
        String sql = """
            SELECT DISTINCT e.employee_id, e.name, e.email, e.password
            FROM employee e
            JOIN task_employee te ON e.employee_id = te.employee_id
            JOIN task t ON te.task_id = t.task_id
            WHERE MONTH(t.start_date) = MONTH(CURDATE())
            AND YEAR(t.start_date) = YEAR(CURDATE())
            ORDER BY e.name
            """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Employee(
                rs.getInt("employee_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                null
        ));
    }



}
