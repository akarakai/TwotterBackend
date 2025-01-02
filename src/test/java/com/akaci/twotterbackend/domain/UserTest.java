package com.akaci.twotterbackend.domain;

import com.akaci.twotterbackend.exceptions.UserAlreadyFollowedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final Logger LOGGER = LogManager.getLogger(UserTest.class);

    private static final User USER = new User(UUID.randomUUID(), "username98", null, null);
    private static final User USER2 = new User(UUID.randomUUID(), "username89", null, null);

    @BeforeEach
    void setUp() {
        Set<User> FOLLOWED = new HashSet<>();
        FOLLOWED.add(new User(UUID.randomUUID(), "username99332", null, null));
        FOLLOWED.add(new User(UUID.randomUUID(), "username98244", null, null));
        FOLLOWED.add(new User(UUID.randomUUID(), "username97344", null, null));
        FOLLOWED.add(new User(UUID.randomUUID(), "username96771", null, null));

        USER.setFollowed(FOLLOWED); // should not do this in code, because it will not update followers
    }

    @Test
    void followUser_followedNotDuplicated_success() {
        int numberFollowed = USER.getFollowed().size();
        USER.follow(USER2);
        assertEquals(numberFollowed + 1, USER.getFollowed().size());
        assertTrue(USER.getFollowed().contains(USER2));

    }

    @Test
    void followUser_followedDuplicated_throwsException() {
        USER.follow(USER2);
        assertThrows(UserAlreadyFollowedException.class, () -> USER.follow(USER2));

    }

    @Test
    void followUser_followedUserHasAdditionalFollower_success() {
        // check first USER not follower of USER2
        assertFalse(USER2.getFollowers().contains(USER));

        USER.follow(USER2);

        // check again
        assertTrue(USER2.getFollowers().contains(USER));
    }
}