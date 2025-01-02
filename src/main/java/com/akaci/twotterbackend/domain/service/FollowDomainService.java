package com.akaci.twotterbackend.domain.service;

import com.akaci.twotterbackend.domain.User;

public interface FollowDomainService {

    void follow(User user, User userToFollow);
}
