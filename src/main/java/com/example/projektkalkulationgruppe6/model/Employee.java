package com.example.projektkalkulationgruppe6.model;

import java.util.List;

public class Employee {

    private Integer employeeId;
    private String name;
    private String email;
    private String password;
    private List<String> competencies;

    //KONSTRUKTØR
    public Employee (Integer employeeId, String name, String email, String password, List<String> competencies){
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.competencies = competencies;
    }

    //TOM KONSTRUKTØR
    public Employee(){}

    //GETTERS
    public Integer getEmployeeId() { return employeeId;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public List<String> getCompetencies() {return competencies;}


    //SETTERS
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId;}
    public void setName(String name) {this.name = name;}
    public void setPassword(String password) {this.password = password;}
    public void setCompetencies(List<String> competencies) {this.competencies = competencies;}


    //TO STRING
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", competencies=" + competencies +
                '}';
    }

}

