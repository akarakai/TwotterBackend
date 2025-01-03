package com.akaci.twotterbackend.domain;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Account {

    private final UUID id;
    private final String username;
    private final String password;
    private final Role role;
    private User user;

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
        this.id = UUID.randomUUID();
    }

    public Account(String username, String password) {
        validateUsername(username);
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        this.id = UUID.randomUUID();
    }

    private void validateUsername(String username) {
        UsernameValidator.validate(username);
    }
}
