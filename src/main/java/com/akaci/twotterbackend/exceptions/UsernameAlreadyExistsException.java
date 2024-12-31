package com.akaci.twotterbackend.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("username already exists");
    }
}
