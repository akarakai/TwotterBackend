package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.service.FollowService;
import com.akaci.twotterbackend.domain.User;
import com.akaci.twotterbackend.domain.service.FollowDomainService;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {

    private static final Logger LOGGER  = LogManager.getLogger(FollowServiceImpl.class);

    private final FollowDomainService followDomainService;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowDomainService followDomainService, UserRepository userRepository) {
        this.followDomainService = followDomainService;
        this.userRepository = userRepository;
    }

    @Override
    public void follow(String username, String usernameToFollow) {
        // TODO
        // trova i user dalla repository e poi usa il follow domain service che encapsula logic di User

    }
}
