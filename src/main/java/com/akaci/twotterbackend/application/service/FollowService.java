package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.domain.User;

public interface FollowService {

    void follow(String username, String usernameToFollow);

}
