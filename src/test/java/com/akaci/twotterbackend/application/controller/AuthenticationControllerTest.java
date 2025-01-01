package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.LogInRequest;
import com.akaci.twotterbackend.application.dto.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationControllerTest {

    private static final String VALID_USERNAME = "username1998";
    private static final String VALID_PASSWORD = "password1998";
    private static final String SECURED_ENDPOINT = "/api/auth/test";
    private static final String ACCOUNT_CREATE_ENDPOINT = "/api/public/account/create";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String COOKIE_NAME = "jwt-token";


    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp_createAccount() throws Exception {
        SignUpRequest request = new SignUpRequest(VALID_USERNAME, VALID_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders
                .post(ACCOUNT_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)));
    }

    @Test
    void performLogin_basic_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURED_ENDPOINT).with(httpBasic(VALID_USERNAME, VALID_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(VALID_USERNAME));
    }

    @Test
    void performLogin_basicWrongCredentials_failure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SECURED_ENDPOINT).with(httpBasic(VALID_USERNAME + "s", VALID_PASSWORD + "da")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void performLogin_noBasic_unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SECURED_ENDPOINT))
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
                .andExpect(cookie().exists(COOKIE_NAME))
                .andExpect(cookie().httpOnly(COOKIE_NAME, true)); // Optionally verify that the cookie is HttpOnly
        ;
    }



}