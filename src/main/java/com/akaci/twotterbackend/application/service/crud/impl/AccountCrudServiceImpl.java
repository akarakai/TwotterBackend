package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.AccountCrudService;
import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountCrudServiceImpl implements AccountCrudService {

    private static final Logger LOGGER = LogManager.getLogger(AccountCrudServiceImpl.class);

    private final AccountRepository accountRepository;

    public AccountCrudServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void updateLastLoggedIn(String accountName) {
        accountRepository.updateLastLogin(accountName, LocalDateTime.now());
    }
}
