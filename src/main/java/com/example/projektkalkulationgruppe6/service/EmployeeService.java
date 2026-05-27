package com.example.projektkalkulationgruppe6.service;


import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.EmployeeNotFoundException;
import com.example.projektkalkulationgruppe6.exception.InvalidEmployeeException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Task;
import com.example.projektkalkulationgruppe6.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskService taskService;

    public EmployeeService(EmployeeRepository employeeRepository, TaskService taskService) {
        this.employeeRepository = employeeRepository;
        this.taskService = taskService;
    }

    public List<Employee> findAll() {

        List<Employee> employees = employeeRepository.findAll();

        if (employees == null) {
            throw new DatabaseOperationException("Employee List could not be found.", new Throwable("NotFound"));
        }

        return employees;
    }

    public Employee findById(int id) {
        Employee employee = employeeRepository.findById(id);

        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }

        return employee;
    }

    public Employee login(String email, String password){
        validateEmail(email);

        Employee employee = employeeRepository.findByEmail(email);
        if(employee != null && employee.getPassword().equals(password)){
            return employee;
        }
        throw new InvalidEmployeeException("Wrong email og password.");

    }



    //PRIVATE VALIDERINGS-METODER
    private void validateId(int id){
        if(id<=0){
            throw new InvalidEmployeeException("Id has to be a positive number.");
        }
    }

    private void validateEmail(String email){
        if(email==null || email.isBlank()){
            throw new InvalidEmployeeException("Email is necessary.");
        }
        if(email.length()>100){
            throw new InvalidEmployeeException("Email can't be over 100 characters.");
        }
        final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        if(!EMAIL_PATTERN.matcher(email).matches()){
            throw new InvalidEmployeeException("Incorrect Email format.");
        }
    }


    private void validateEmployee(Employee employee){

        if(employee == null){
            throw new InvalidEmployeeException("Employee is null.");
        }
        String EmployeeName = employee.getName();
        if(EmployeeName == null || EmployeeName.isBlank()){
            throw new InvalidEmployeeException("Name is necessary.");
        }
        if(EmployeeName.length() > 50){
            throw new InvalidEmployeeException("Name cant be over 100 characters.");
        }

        validateEmail(employee.getEmail());

    }


    private BigDecimal calculateWorkloadPercentage(BigDecimal totalHours) {
        if (totalHours == null || totalHours.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalHours
                .divide(new BigDecimal("160"), 1, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    public List<String> getResourceLoadCurrentMonth() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        List<Employee> employees = employeeRepository.findAllWithTasksCurrentMonth();
        List<String> result = new ArrayList<>();

        for (Employee e : employees) {
            BigDecimal totalHours = employeeRepository.sumEstimatedHoursByEmployeeAndMonth(
                    e.getEmployeeId(), year, month);
            BigDecimal percentage = calculateWorkloadPercentage(totalHours);
            result.add(e.getName() + "|" + percentage);
        }

        return result;
    }

}
