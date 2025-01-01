package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.LogInRequest;
import com.akaci.twotterbackend.application.dto.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BaseAuthenticationTest {

    protected static final Logger LOGGER = LogManager.getLogger(BaseAuthenticationTest.class);

    protected static final String VALID_USERNAME = "username1998";
    protected static final String VALID_PASSWORD = "password1998";
    protected static final String ACCOUNT_CREATE_ENDPOINT = "/api/public/account/create";
    protected static final String LOGIN_ENDPOINT = "/api/auth/login";
    protected static final String JWT_COOKIE_NAME = "jwt-token";

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper mapper = new ObjectMapper();
    protected Cookie jwtCookie;

    @BeforeEach
    protected void setUp_createAccount() throws Exception {
        createAccount();
        jwtCookie = getJwtCookie();
    }

    protected void createAccount() throws Exception {
        SignUpRequest request = new SignUpRequest(VALID_USERNAME, VALID_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders
                .post(ACCOUNT_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

    protected Cookie getJwtCookie() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new LogInRequest(VALID_USERNAME, VALID_PASSWORD))))
                .andReturn();

        return mvcResult.getResponse().getCookie(JWT_COOKIE_NAME);
    }
}
