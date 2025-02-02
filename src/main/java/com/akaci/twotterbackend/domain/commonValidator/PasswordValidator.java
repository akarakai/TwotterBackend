package com.akaci.twotterbackend.domain.commonValidator;

import com.akaci.twotterbackend.exceptions.PasswordRefusedException;


// 1. The password must have at least 8 characters (MIN_LENGTH).
// 2. The password must contain at least one letter (uppercase or lowercase).
// 3. The password must contain at least one digit.
// If the password does not meet these criteria, a PasswordRefusedException is thrown.
public class PasswordValidator {

    private static final int MIN_LENGTH = 8;

    public static void validate(String password) throws PasswordRefusedException {
        if (password == null || password.length() < MIN_LENGTH || password.isBlank()) {
            throw new PasswordRefusedException();
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            throw new PasswordRefusedException();
        }
        if (!password.matches(".*\\d.*")) {
            throw new PasswordRefusedException();
        }
    }
}