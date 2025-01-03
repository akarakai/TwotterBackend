package com.akaci.twotterbackend.domain.service;

import com.akaci.twotterbackend.domain.model.User;

public interface FollowDomainService {

    void follow(User user, User userToFollow);
    void unfollow(User user, User userToUnfollow);

}
