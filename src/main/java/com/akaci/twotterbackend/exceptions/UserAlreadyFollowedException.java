package com.akaci.twotterbackend.exceptions;

public class UserAlreadyFollowedException extends RuntimeException {
    public UserAlreadyFollowedException() {
        super("user already followed");
    }
}
