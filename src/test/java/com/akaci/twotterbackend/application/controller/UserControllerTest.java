package com.akaci.twotterbackend.application.controller;

import jakarta.servlet.http.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseAuthenticationTest {

    private static final Logger LOGGER = LogManager.getLogger(UserControllerTest.class);
    private static final String ANOTHER_USERNAME = "ginoPippo198";
    private static final String ANOTHER_PASSWORD = "password123";

    private Cookie anotherJwtCookie;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() throws Exception {
        // create another account to follow
        createAccount(ANOTHER_USERNAME, ANOTHER_PASSWORD);
//        anotherJwtCookie = getJwtCookie(ANOTHER_USERNAME, ANOTHER_PASSWORD);
    }

    @Test
    void followUser_validUsernames_userFollowed() throws Exception {
        performFollowRequest()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ANOTHER_USERNAME));;
    }

    @Test
    void followUser_userAlreadyFollowed_badRequestResponse() throws Exception {
        performFollowRequest();
        performFollowRequest()
                .andExpect(status().isBadRequest());

    }

    private ResultActions performFollowRequest() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                        .post(getEndpoint(ANOTHER_USERNAME)).cookie(jwtDefaultUser));
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value(ANOTHER_USERNAME));
    }

    private String getEndpoint(String username) {
        return "/api/user/" + username + "/follow";
    }













}