package com.akaci.twotterbackend.domain;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.exceptions.UsernameNotValidException;
import lombok.Getter;

@Getter
public class Profile {

    private final String username;
    private String description;

    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public Profile(String username, String description) {
        validateName(username);
        setDescription(description);
        this.username = username;
    }

    public void setDescription(String description) {
        if (description != null) {
            if (description.length() > MAX_DESCRIPTION_LENGTH) {
                throw new IllegalArgumentException("description of the username is not valid");
            }
        }

        this.description = (description == null) ? "" : description;

    }

    private void validateName(String username) {
        UsernameValidator.validate(username);
    }
}
