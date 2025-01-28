package com.akaci.twotterbackend.exceptions;

public class LoginExeption extends RuntimeException {
    public LoginExeption() {
        super("incorrect username or password");
    }
}
