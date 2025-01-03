package com.akaci.twotterbackend.exceptions;

public class UserToFollowNotFoundException extends RuntimeException {
    public UserToFollowNotFoundException() {
        super("username wanted to follow does not exist");
    }
}
