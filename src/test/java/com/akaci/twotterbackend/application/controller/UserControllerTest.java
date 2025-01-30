package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseAuthenticationTest {

    private static final Logger LOGGER = LogManager.getLogger(UserControllerTest.class);
    private static final String ANOTHER_USERNAME = "ginoPippo198";
    private static final String ANOTHER_PASSWORD = "password123";
    private static final String USERNAME_INEXISTENT_TO_FOLLOW = "skibidi000";

    private Cookie anotherJwtCookie;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        // create another account to follow
        createAccount(ANOTHER_USERNAME, ANOTHER_PASSWORD);
    }

    private String getFollowEndpoint(String username) {
        return "/api/user/" + username + "/follow";
    }

    private String getFollowEndpoint(UUID id) {
        return "/api/user/id/" + id.toString() + "/follow";
    }

    private String getUnfollowEndpoint(String username) {
        return "/api/user/" + username + "/unfollow";
    }

    private String getUnfollowEndpoint(UUID id) {
        return "/api/user/id/" + id.toString() + "/unfollow";
    }


    @Test
    void followUser_validUsernames_userFollowed() throws Exception {
        performFollowRequest(ANOTHER_USERNAME)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ANOTHER_USERNAME));;
    }

    @Test
    void followUser_userAlreadyFollowed_badRequestResponse() throws Exception {
        performFollowRequest(ANOTHER_USERNAME);
        performFollowRequest(ANOTHER_USERNAME)
                .andExpect(status().isBadRequest());
    }

    @Test
    void followUser_usernameDoesNotExist_badRequest() throws Exception {
        performFollowRequest(USERNAME_INEXISTENT_TO_FOLLOW)
                .andExpect(status().isBadRequest());
    }


    @Test
    void followUser_goodId_userFollower() throws Exception {
        Optional<UserJpaEntity> userCreatedNew = userRepository.findByUsername(ANOTHER_USERNAME);
        assert userCreatedNew.isPresent();
        UserJpaEntity user = userCreatedNew.get();
        performFollowRequest(user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ANOTHER_USERNAME))
                .andExpect(jsonPath("$.id").value(user.getId().toString()));
    }

    @Test
    void followUser_notAnUUID_badRequestResponse() throws Exception {
        String badId = "im-totally-not-an-uuid-LMAO";
        performFollowRequest("/api/user/id/" + badId + "/follow")
                .andExpect(status().isNotFound());
    }

    @Test
    void unfollowUser_userAlreadyFollowed_userRemovedFromFollowedAndOtherFromFollowers() throws Exception {
        performFollowRequest(ANOTHER_USERNAME);
        performUnfollowRequest(ANOTHER_USERNAME)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(ANOTHER_USERNAME));
    }

    @Test
    void unfollowUser_userWasNotFollowed_badRequestResponse() throws Exception {
        performUnfollowRequest(ANOTHER_USERNAME)
                .andExpect(status().isBadRequest());
    }

    @Test
    void unfollowUser_goodId_success() throws Exception {
        performFollowRequest(ANOTHER_USERNAME);

        Optional<UserJpaEntity> userCreatedNew = userRepository.findByUsername(ANOTHER_USERNAME);
        assert userCreatedNew.isPresent();
        UserJpaEntity user = userCreatedNew.get();

        performUnfollowRequest(user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ANOTHER_USERNAME));
    }

    @Test
    void unfollowUser_notAnUUID_badRequestResponse() throws Exception {
        String badId = "im-totally-not-an-uuid-LMAO";
        performFollowRequest(ANOTHER_USERNAME);
        performUnfollowRequest("/api/user/id/" + badId + "/unfollow")
        .andExpect(status().isNotFound());

    }


    private ResultActions performFollowRequest(String username) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                        .post(getFollowEndpoint(username)).cookie(jwtDefaultUser));
    }

    private ResultActions performFollowRequest(UUID id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(getFollowEndpoint(id)).cookie(jwtDefaultUser));
    }

    private ResultActions performUnfollowRequest(String username) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(getUnfollowEndpoint(username)).cookie(jwtDefaultUser));
    }

    private ResultActions performUnfollowRequest(UUID id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(getUnfollowEndpoint(id)).cookie(jwtDefaultUser));
    }















}