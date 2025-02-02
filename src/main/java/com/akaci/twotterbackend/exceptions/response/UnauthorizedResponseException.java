package com.akaci.twotterbackend.exceptions.response;

public class UnauthorizedResponseException extends RuntimeException {
    public UnauthorizedResponseException() {
        super("authentication failed");
    }
}
