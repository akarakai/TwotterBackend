package com.akaci.twotterbackend.domain.commonValidator;

import com.akaci.twotterbackend.exceptions.UsernameNotValidException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UsernameValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {".ginoPino", "gino.Pino", "1ginoIlPino", "thisUsernameHasMoreThanTwentyCharacters", "_thisIsInvalid",
            "?badUsername", "bad@Username", "$%^&*()_+=-"})
    void createAccount_invalidUsername_accountMade(String username) {
        assertThrows(UsernameNotValidException.class, () -> UsernameValidator.validate(username));
    }

}