package com.example.projektkalkulationgruppe6.exception;

public class NotLoggedInException extends RuntimeException{
    public NotLoggedInException() {
        super("You need to be logged in to view this page.");
    }

}
