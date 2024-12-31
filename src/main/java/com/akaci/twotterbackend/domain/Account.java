package com.akaci.twotterbackend.domain;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import lombok.Getter;

@Getter
public class Account {

    private final String username;
    private final String password;
    private final Role role;

    public Account(String username, String password, Role role) {
        validateUsername(username);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Account(String username, String password) {
        validateUsername(username);
        this.username = username;
        this.password = password;
        this.role = Role.USER;
    }

    private void validateUsername(String username) {
        UsernameValidator.validate(username);
    }
}
