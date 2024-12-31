package com.akaci.twotterbackend.domain.commonValidator;

import com.akaci.twotterbackend.exceptions.UsernameNotValidException;

public class UsernameValidator {

    private static final int MAX_USERNAME_LENGTH = 20;

    public static void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new UsernameNotValidException();
        }

        if (username.length() > MAX_USERNAME_LENGTH) {
            throw new UsernameNotValidException();
        }

        if (username.contains(".")) {
            throw new UsernameNotValidException();
        }

        if (Character.isDigit(username.charAt(0)) || username.startsWith("_")) {
            throw new UsernameNotValidException();
        }

        if (!username.matches("[A-Za-z0-9_]+")) {
            throw new UsernameNotValidException();
        }
    }

}
