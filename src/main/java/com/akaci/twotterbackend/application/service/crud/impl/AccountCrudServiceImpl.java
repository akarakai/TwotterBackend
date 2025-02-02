package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.AccountCrudService;
import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountCrudServiceImpl implements AccountCrudService {

    private static final Logger LOGGER = LogManager.getLogger(AccountCrudServiceImpl.class);

    private final AccountRepository accountRepository;

    public AccountCrudServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void updateLastLoggedIn(String accountName) {
        Optional<AccountEntity> accountOptional = accountRepository.findByUsername(accountName);
        if (accountOptional.isEmpty()) {
            throw new UsernameNotFoundException("username not fount");
        }
        AccountEntity account = accountOptional.get();
        account.setLastLoggedInAt(LocalDateTime.now());
        accountRepository.save(account);

    }
}
