package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.service.AuthenticationService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.model.Account;
import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.commonValidator.PasswordValidator;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.exceptions.UsernameAlreadyExistsException;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.ProfileJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import com.akaci.twotterbackend.persistence.mapper.AccountEntityMapper;
import com.akaci.twotterbackend.persistence.mapper.ProfileEntityMapper;
import com.akaci.twotterbackend.persistence.mapper.UserEntityMapper;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationServiceImpl.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCrudService userCrudService;

    public AuthenticationServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder,
                                     UserCrudService userCrudService, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCrudService = userCrudService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Account signUp(String username, String password) {
        UsernameValidator.validate(username);
        PasswordValidator.validate(password);
        Optional<AccountJpaEntity> opAccountEntity = accountRepository.findByUsername(username);
        if (opAccountEntity.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
        String hashedPassword = passwordEncoder.encode(password);

        Profile profile = Profile.builder()
                .username(username)
                .description("")
                .build();

        User user = User.builder()
                .username(username)
                .profile(profile)
                .build();

        Account account = Account.builder()
                .username(username)
                .password(hashedPassword)
                .user(user)
                .role(Role.USER)
                .build();

        ProfileJpaEntity profileJpa = ProfileEntityMapper.toJpaEntity(profile);
        UserJpaEntity userJpa = UserEntityMapper.toJpaEntity(user);
        AccountJpaEntity accountJpa = AccountEntityMapper.toJpaEntity(account);

        accountJpa.setCreatedAt(LocalDateTime.now());
        AccountJpaEntity savedEntity = accountRepository.save(accountJpa);
        return AccountEntityMapper.toDomain(savedEntity);




        // THIS WORKSa
//        // without using mapper
//        // create user
//        AccountJpaEntity accountJpaEntity = new AccountJpaEntity();
//        accountJpaEntity.setUsername(username);
//        accountJpaEntity.setPassword(hashedPassword);
//        accountJpaEntity.setCreatedAt(LocalDateTime.now());
//        accountJpaEntity.setRole(new RoleJpaEntity(Role.USER));
//
//        ProfileJpaEntity profileJpaEntity = new ProfileJpaEntity(username, "");
//
//        UserJpaEntity userJpaEntity = new UserJpaEntity();
//        userJpaEntity.setUsername(username);
//        userJpaEntity.setProfile(profileJpaEntity);
//
//        accountJpaEntity.setUser(userJpaEntity);
//        // save to repository
//        AccountJpaEntity savedAccountJpaEntity = accountRepository.save(accountJpaEntity);
//        LOGGER.info("SKIBIDI");
//        return AccountEntityMapper.toDomain(savedAccountJpaEntity);









//        // Create User and Profile first
//        User user = new User(username, new Profile(username, null));
//        UserJpaEntity userJpaEntity = UserEntityMapper.toJpaEntity(user);
//
//        // Save User entity first
//        UserJpaEntity savedUserEntity = userRepository.save(userJpaEntity);
//
//        // Create Account with the saved User entity
//        Account account = Account.builder()
//                .username(username)
//                .password(hashedPassword)
//                .user(UserEntityMapper.toDomain(savedUserEntity))  // Use the saved user
//                .role(Role.USER)
//                .build();
//
//        // Convert to JPA entity and set the saved user
//        AccountJpaEntity accountJpaEntity = AccountEntityMapper.toJpaEntity(account);
//        accountJpaEntity.setUser(savedUserEntity);
//        accountJpaEntity.setCreatedAt(LocalDateTime.now());
//
//        // Save Account entity
//        AccountJpaEntity savedAccountEntity = accountRepository.save(accountJpaEntity);
//
//        return AccountEntityMapper.toDomain(savedAccountEntity);
    }
}
