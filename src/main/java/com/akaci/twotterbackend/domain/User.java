package com.akaci.twotterbackend.domain;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;

import java.util.List;

public class User {

    private final String name;
    private final Profile profile;
    private final Account account;

    public User(String name, Profile profile, Account account) {
        validateName(name);
        this.name = name;
        this.profile = profile;
        this.account = account;
    }

    private void validateName(String name) {
        UsernameValidator.validate(name);
    }

}
