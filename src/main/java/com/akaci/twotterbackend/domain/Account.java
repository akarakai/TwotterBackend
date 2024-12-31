package com.akaci.twotterbackend.domain;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Account {

    private UUID id;
    private final String username;
    private final String password;
    private final Role role;

    public Account(UUID id, String username, String password, Role role) {
        this.id = id;
        validateUsername(username);
        this.username = username;
        this.password = password;
        this.role = role;
    }



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
