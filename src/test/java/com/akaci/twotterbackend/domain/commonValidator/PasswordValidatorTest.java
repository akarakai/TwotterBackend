package com.akaci.twotterbackend.domain.commonValidator;

import com.akaci.twotterbackend.exceptions.PasswordRefusedException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"Password123", "Secure1234", "StrongPass8", "P@ssw0rd123", "ValidPass2024"})
    void goodPassword(String password) {
        assertDoesNotThrow(() -> PasswordValidator.validate(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"short", "abcdefgh", "12345678", "Pass1"})
    void badPassword(String password) {
        assertThrows(PasswordRefusedException.class, () -> PasswordValidator.validate(password));
    }



}