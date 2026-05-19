package com.example.projektkalkulationgruppe6.exception;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(int id) {
        super("The Task with id " + id + " could not be found.");
    }
}
