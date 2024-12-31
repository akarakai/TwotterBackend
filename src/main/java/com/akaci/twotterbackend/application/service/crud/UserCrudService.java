package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.domain.User;

public interface UserCrudService {

    User createUserFromAccount(User user, Account account);

}
