package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.LogInRequest;
import com.akaci.twotterbackend.application.dto.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfileControllerTest {

    // TODO MANY TEST CLASSES REQUIRE LOGIN/CREATION OF ACCOUNT. CREATE CLASS THAT DOES THIS THING FOR YOU SO THAT THERE IS NO NEED

    private static final Logger LOGGER = LogManager.getLogger(ProfileControllerTest.class);

    private static final String VALID_USERNAME = "username1998";
    private static final String VALID_PASSWORD = "password1998";
    private static final String GET_PROFILE_ENDPOINT = "/api/profile";
    private static final String ACCOUNT_CREATE_ENDPOINT = "/api/public/account/create";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String JWT_COOKIE_NAME = "jwt-token";


    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();
    private Cookie jwtCookie;

    @BeforeEach
    void setUp_createAccount() throws Exception {
       createAccount();
       jwtCookie = getJwtCookie();
    }

    @Test
    void getProfile_successful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(GET_PROFILE_ENDPOINT)
                .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileName").value(VALID_USERNAME))
                .andExpect(jsonPath("$.description").value(""));

    }

    private void createAccount() throws Exception {
        SignUpRequest request = new SignUpRequest(VALID_USERNAME, VALID_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders
                .post(ACCOUNT_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

    private Cookie getJwtCookie() throws Exception {
        // login in order to get cookie
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new LogInRequest(VALID_USERNAME, VALID_PASSWORD)))).andReturn();

        return mvcResult.getResponse().getCookie(JWT_COOKIE_NAME);
    }
}