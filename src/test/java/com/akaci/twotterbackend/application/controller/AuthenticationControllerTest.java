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


class AuthenticationControllerTest extends BaseAuthenticationTest {
    private static final String SECURED_ENDPOINT = "/api/auth/test";

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
                .andExpect(cookie().exists(JWT_COOKIE_NAME))
                .andExpect(cookie().httpOnly(JWT_COOKIE_NAME, true));
    }
}