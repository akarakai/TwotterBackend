package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.domain.model.Account;

public interface AuthenticationService {

    Account signUp(String username, String password);

}
