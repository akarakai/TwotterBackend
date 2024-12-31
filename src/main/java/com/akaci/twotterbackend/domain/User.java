package com.akaci.twotterbackend.domain;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {

    private UUID id;
    private String username;
    private Profile profile;
    private Account account;

    public User(UUID id, String username, Profile profile, Account account) {
        this.id = id;
        validateName(username);
        this.username = username;
        this.profile = profile;
        this.account = account;
    }

    public User(String username, Profile profile, Account account) {
        validateName(username);
        this.username = username;
        this.profile = profile;
        this.account = account;
    }

    public User(String username) {
        validateName(username);
        this.username = username;
    }

    private void validateName(String name) {
        UsernameValidator.validate(name);
    }

}
