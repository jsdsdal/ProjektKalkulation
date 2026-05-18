package com.example.projektkalkulationgruppe6.repository;

import com.example.projektkalkulationgruppe6.model.Employee;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
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


}
