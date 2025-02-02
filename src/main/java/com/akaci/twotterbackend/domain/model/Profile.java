package com.akaci.twotterbackend.domain.model;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class Profile {

    private final String name;
    private String description;

    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public Profile(String name, String description) {
        validateName(name);
        setDescription(description);
        this.name = name;
    }

    public Profile(String name) {
        validateName(name);
        setDescription(null);
        this.name = name;

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
