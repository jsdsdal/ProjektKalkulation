package com.example.projektkalkulationgruppe6.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(int id) {
        super("The Project with id " + id + " could not be found.");
    }
}
