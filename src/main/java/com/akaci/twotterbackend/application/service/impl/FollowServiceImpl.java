package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.service.FollowService;
import com.akaci.twotterbackend.domain.User;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.domain.service.FollowDomainService;
import com.akaci.twotterbackend.exceptions.UsernameAlreadyExistsException;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.entity.joinEntity.embeddedId.FollowSystemId;
import com.akaci.twotterbackend.persistence.entity.joinEntity.follow.FollowUserJpaEntity;
import com.akaci.twotterbackend.persistence.mapper.UserEntityMapper;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    @Transactional
    public User follow(String username, String usernameToFollow) {
        UsernameValidator.validate(usernameToFollow);
        if (username == null || username.isEmpty() || usernameToFollow.isEmpty()) {
            throw new IllegalArgumentException("Username or Follow Name cannot be empty");
        }

        if (username.equals(usernameToFollow)) {
            throw new UsernameAlreadyExistsException(); // TODO an other exception
        }

        Optional<UserJpaEntity> opUser = userRepository.findByUsername(username);
        assert opUser.isPresent(); // because already logged in
        Optional<UserJpaEntity> opUserToFollow = userRepository.findByUsername(usernameToFollow);
        if (opUserToFollow.isEmpty()) {
            throw new UsernameNotFoundException("user to follow does not exist");
        }

        UserJpaEntity userToFollow = opUserToFollow.get();
        UserJpaEntity user = opUser.get();

        User userDomain = UserEntityMapper.toDomain(user);
        User userToFollowDomain = UserEntityMapper.toDomain(userToFollow);

        followDomainService.follow(userDomain, userToFollowDomain);

        // reconvert and save to repository
        userRepository.save(UserEntityMapper.toJpaEntity(userToFollowDomain));
        userRepository.save(UserEntityMapper.toJpaEntity(userDomain));

        return userToFollowDomain;

    }
}


