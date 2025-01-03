package com.akaci.twotterbackend.exceptions.response;

public class BadRequestExceptionResponse extends RuntimeException {
    public BadRequestExceptionResponse(String message) {
        super(message);
    }
}
