package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.domain.model.User;

import java.util.UUID;

public interface FollowService {

    User followUserByUsername(String username, String usernameToFollow);
    User followUserById(String username, UUID id);

    User unfollowUserByUsername(String username, String usernameToUnfollow);
    User unfollowUserById(String username, UUID id);

}
