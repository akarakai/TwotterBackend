package com.akaci.twotterbackend.exceptions;

public class PasswordRefusedException extends RuntimeException {
    public PasswordRefusedException() {
        super("there is something wrong with the password");
    }
}
