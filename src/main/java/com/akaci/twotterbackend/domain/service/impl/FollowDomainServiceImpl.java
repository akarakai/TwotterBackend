package com.akaci.twotterbackend.domain.service.impl;

import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.service.FollowDomainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FollowDomainServiceImpl implements FollowDomainService {

    private static final Logger logger = LogManager.getLogger(FollowDomainServiceImpl.class);

    @Override
    public void follow(User user, User userToFollow) {
        validateParameters(user, userToFollow);
        user.follow(userToFollow);
    }

    @Override
    public void unfollow(User user, User userToUnfollow) {
        validateParameters(user, userToUnfollow);
        user.unfollow(userToUnfollow);
    }


    private void validateParameters(User user1, User user2) {
        if (user1 == null || user2 == null) {
            throw new NullPointerException();
        }
    }
}
