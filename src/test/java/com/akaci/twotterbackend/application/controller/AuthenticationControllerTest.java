package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.LogInRequest;
import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AuthenticationControllerTest extends BaseAuthenticationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void performLogin_goodCredentials_loginSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new LogInRequest(VALID_USERNAME, VALID_PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(VALID_USERNAME));
    }

    @Test
    void performLogin_badUsername_unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new LogInRequest(VALID_USERNAME + "asd", VALID_PASSWORD))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void performLogin_badPassword_unAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new LogInRequest(VALID_USERNAME, VALID_PASSWORD + "asd"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void performLogin_loginOk_jwtCookieCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new LogInRequest(VALID_USERNAME, VALID_PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(VALID_USERNAME))
                .andExpect(cookie().exists(JWT_COOKIE_NAME))
                .andExpect(cookie().httpOnly(JWT_COOKIE_NAME, true));
    }


    @Test
    void performLogin_loginSuccessful_accountLastLoginUpdated() throws Exception {
        AccountEntity accountEntity = accountRepository.findByUsername(VALID_USERNAME).get();
        LocalDateTime firstLogin = accountEntity.getLastLoggedInAt();

        Thread.sleep(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new LogInRequest(VALID_USERNAME, VALID_PASSWORD))))
                .andExpect(status().isOk());

        AccountEntity refreshedEntity = accountRepository.findByUsername(VALID_USERNAME).get();
        LocalDateTime secondLogin = refreshedEntity.getLastLoggedInAt();

        assertTrue(firstLogin.isBefore(secondLogin));
    }

}