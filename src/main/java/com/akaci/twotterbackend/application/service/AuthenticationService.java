package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.application.dto.response.LogInResponse;
import com.akaci.twotterbackend.application.dto.response.SignUpResponse;
import com.akaci.twotterbackend.domain.commonValidator.PasswordValidator;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.domain.model.Account;
import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.domain.model.user.User;
import com.akaci.twotterbackend.exceptions.UsernameAlreadyExistsException;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.exceptions.response.UnauthorizedResponseException;
import com.akaci.twotterbackend.persistence.entity.AccountEntity;

import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import com.akaci.twotterbackend.persistence.mapper.AccountMapper;
import com.akaci.twotterbackend.persistence.mapper.UserMapper;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import com.akaci.twotterbackend.security.authentication.jwt.JwtUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthenticationService {

    private final AccountRepository accountRepo;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final AccountMapper accountMapper;
    private final UserMapper userMapper;

    public AuthenticationService(AccountRepository accountRepo, AuthenticationManager authManager, PasswordEncoder passwordEncoder,
                                 AccountMapper accountMapper, UserMapper userMapper, JwtUtil jwtUtil) {
        this.accountRepo = accountRepo;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public SignUpResponse createAccount(String username, String password) {
        validateCredentials(username, password);

        Optional<AccountEntity> foundAccount = accountRepo.findByUsername(username);
        if (foundAccount.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        String hashedPassword = passwordEncoder.encode(password);

        Profile profile = new Profile(username);
        User user = User.builder()
                .username(username)
                .profile(profile)
                .build();
        Account account = Account.builder()
                .username(username)
                .password(hashedPassword)
                .role(Role.USER)
                .build();

        UserEntity userEntity = userMapper.toEntity(user);
        AccountEntity accountEntity = accountMapper.toEntity(account);

        accountEntity.setUser(userEntity);
        accountRepo.save(accountEntity);

        return new SignUpResponse(username);
    }

    @Transactional
    public LogInResponse login(String username, String password, HttpServletResponse response) {
        validateCredentials(username, password);

        Authentication auth = performLogin(username, password);
        if (auth == null) {
            log.error("Authentication is null");
            throw new BadRequestExceptionResponse("Authentication is null");
        }

        try {
            Cookie jwtCookie = generateCookie(auth);
            response.addCookie(jwtCookie);
            return new LogInResponse(username);
        } catch (NoSuchAlgorithmException | JOSEException e) {
            log.error(e.getMessage());
            throw new UnauthorizedResponseException();
        }
    }

    private void validateCredentials(String username, String password) {
        UsernameValidator.validate(username);
        PasswordValidator.validate(password);
    }

    private Authentication performLogin(String username, String password) {
        Authentication preAuth = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication auth;
        try {
            auth = authManager.authenticate(preAuth);
            if (auth.isAuthenticated()) {
                log.info("User {} authenticated", username);
                accountRepo.updateLastLogIn(username, LocalDateTime.now());
                return auth;
            }
        } catch (AuthenticationException e) {
            throw new UnauthorizedResponseException();
        }
        return null;
    }

    private Cookie generateCookie(Authentication auth) throws NoSuchAlgorithmException, JOSEException {
        var authorities = auth.getAuthorities();
        String jwt = jwtUtil.generateJwt(auth.getName(), authorities);
        Cookie cookie = new Cookie("jwt-token", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
