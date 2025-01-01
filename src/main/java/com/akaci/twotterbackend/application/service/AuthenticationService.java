package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.domain.Account;

public interface AuthenticationService {

    Account signUp(String username, String password);

}
