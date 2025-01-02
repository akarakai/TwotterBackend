package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.AccountCrudService;
import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<AccountJpaEntity> accountOptional = accountRepository.findByUsername(accountName);
        if (accountOptional.isEmpty()) {
            throw new UsernameNotFoundException("username not fount");
        }
        AccountJpaEntity account = accountOptional.get();
        account.setLastLoggedInAt(LocalDateTime.now());
        accountRepository.save(account);

    }
}
