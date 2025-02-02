package com.akaci.twotterbackend.domain.model;

import com.akaci.twotterbackend.domain.commonValidator.PasswordValidator;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Account {

    private final UUID id;
    private final String username;
    private final String password;
    private final Role role;

    public Account(String username, String password) {
        validateUsername(username);
        this.password = password;
        this.username = username;
        this.role = Role.USER;
        this.id = UUID.randomUUID();

    }

    private void validateUsername(String username) {
        UsernameValidator.validate(username);
    }


}
