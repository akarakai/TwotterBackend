package com.akaci.twotterbackend.exceptions;

public class UsernameNotValidException extends RuntimeException {
    public UsernameNotValidException() {
        super("username not valid");
    }
}
