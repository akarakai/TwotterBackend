package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.domain.model.Account;
import com.akaci.twotterbackend.domain.model.User;

public interface UserCrudService {

    User createUserFromAccount(User user, Account account);
    User findByAccount(Account account);
    User findByUsername(String username);

}
