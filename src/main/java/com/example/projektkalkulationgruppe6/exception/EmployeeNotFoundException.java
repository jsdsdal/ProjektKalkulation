package com.example.projektkalkulationgruppe6.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(int id) {
        super("Employee with " + id + " was not found.");
    }

}
