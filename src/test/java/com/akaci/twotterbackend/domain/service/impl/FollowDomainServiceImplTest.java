package com.akaci.twotterbackend.domain.service.impl;

import com.akaci.twotterbackend.domain.User;
import com.akaci.twotterbackend.domain.service.FollowDomainService;
import com.akaci.twotterbackend.exceptions.UserAlreadyFollowedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FollowDomainServiceImplTest {

    private static final Logger LOGGER = LogManager.getLogger(FollowDomainServiceImplTest.class);
    private User USER;
    private User USER_TO_FOLLOW;
    private final FollowDomainService followDomainService = new FollowDomainServiceImpl();


    @BeforeEach
    void setUp() {
        USER = User.builder().id(UUID.randomUUID()).username("userOne123").build();
        USER_TO_FOLLOW = User.builder().id(UUID.randomUUID()).username("userOne124").build();
    }
    @Test
    void followUser2_user2IsNotAlreadyFollowed_success() {

        followDomainService.follow(USER, USER_TO_FOLLOW);
        Set<User> followed = USER.getFollowed();
        Set<User> followers = USER_TO_FOLLOW.getFollowers();
        assertTrue(followed.contains(USER_TO_FOLLOW));
        assertTrue(followers.contains(USER));
    }

    @Test
    void followUser2_user2IsAlreadyFollowed_throwsException() {
        Set<User> followed = new HashSet<>();
        followed.add(USER_TO_FOLLOW);
        USER.setFollowed(followed);
        assertThrows(UserAlreadyFollowedException.class, () -> followDomainService.follow(USER, USER_TO_FOLLOW));
    }




}