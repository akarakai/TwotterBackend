package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.service.AuthenticationService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.domain.User;
import com.akaci.twotterbackend.domain.commonValidator.PasswordValidator;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.exceptions.UsernameAlreadyExistsException;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.RoleJpaEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import com.akaci.twotterbackend.persistence.mapper.AccountEntityMapper;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationServiceImpl.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCrudService userCrudService;

    public AuthenticationServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, UserCrudService userCrudService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCrudService = userCrudService;
    }

    @Override
    public Account signUp(String username, String password) {
        UsernameValidator.validate(username);
        PasswordValidator.validate(password);
        Optional<AccountJpaEntity> opAccountEntity = accountRepository.findByUsername(username);
        if (opAccountEntity.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }


        String hashedPassword = passwordEncoder.encode(password);
        AccountJpaEntity savedAccountEntity = accountRepository.save(new AccountJpaEntity(username, hashedPassword, Role.USER));

        // after saving the account, create a new user with same username and a null description
        User newUser = new User(savedAccountEntity.getUsername());

        // persist the username along with the default profile using the crud service
        userCrudService.createUserFromAccount(newUser, AccountEntityMapper.toDomain(savedAccountEntity));

        return AccountEntityMapper.toDomain(savedAccountEntity);
    }

    @Override
    public Account logIn(String username, String password) {
        return null;
    }


}
