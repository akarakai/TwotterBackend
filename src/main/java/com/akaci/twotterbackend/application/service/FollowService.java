package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.domain.User;

public interface FollowService {

    User follow(String username, String usernameToFollow);

}
