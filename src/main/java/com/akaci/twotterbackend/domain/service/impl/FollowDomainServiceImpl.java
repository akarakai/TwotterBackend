package com.akaci.twotterbackend.domain.service.impl;

import com.akaci.twotterbackend.domain.User;
import com.akaci.twotterbackend.domain.service.FollowDomainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FollowDomainServiceImpl implements FollowDomainService {

    private static final Logger logger = LogManager.getLogger(FollowDomainServiceImpl.class);

    @Override
    public void follow(User user, User userToFollow) {
        if (user == null || userToFollow == null) {
            throw new NullPointerException();
        }

        user.follow(userToFollow);


    }
}
