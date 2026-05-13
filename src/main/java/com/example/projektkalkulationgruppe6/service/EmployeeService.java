package com.example.projektkalkulationgruppe6.service;


import com.example.projektkalkulationgruppe6.exception.EmployeeNotFoundException;
import com.example.projektkalkulationgruppe6.exception.InvalidEmployeeException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee login(String email, String password){
        validateEmail(email);

        Employee employee = employeeRepository.findByEmail(email);
        if(employee != null && employee.getPassword().equals(password)){
            return employee;
        }
        throw new InvalidEmployeeException("Fokert email eller adgangskode.");

    }



    //PRIVATE VALIDERINGS-METODER
    private void validateId(int id){
        if(id<=0){
            throw new InvalidEmployeeException("Id skal være et positivt tal.");
        }
    }

    private void validateEmail(String email){
        if(email==null || email.isBlank()){
            throw new InvalidEmployeeException("Email er nødvendigt.");
        }
        if(email.length()>100){
            throw new InvalidEmployeeException("Email må ikke overskride 100 karaktere.");
        }
        final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        if(!EMAIL_PATTERN.matcher(email).matches()){
            throw new InvalidEmployeeException("Email formatet er forkert.");
        }
    }


    private void validateEmployee(Employee employee){

        if(employee == null){
            throw new InvalidEmployeeException("Employee er null.");
        }
        String EmployeeName = employee.getName();
        if(EmployeeName == null || EmployeeName.isBlank()){
            throw new InvalidEmployeeException("Navn er nødvendigt.");
        }
        if(EmployeeName.length() > 50){
            throw new InvalidEmployeeException("Navn må ikke overskride 50 karakterer.");
        }

        validateEmail(employee.getEmail());

    }


}
