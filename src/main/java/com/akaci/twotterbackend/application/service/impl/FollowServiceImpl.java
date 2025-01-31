package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.dto.response.FollowUserResponse;
import com.akaci.twotterbackend.application.dto.response.FollowUserResponseList;
import com.akaci.twotterbackend.application.service.FollowService;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.domain.service.FollowDomainService;
import com.akaci.twotterbackend.exceptions.UserToFollowNotFoundException;
import com.akaci.twotterbackend.exceptions.UsernameAlreadyExistsException;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.mapper.UserEntityMapper;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;


@Service
public class FollowServiceImpl implements FollowService {

    private static final Logger LOGGER = LogManager.getLogger(FollowServiceImpl.class);

    private final FollowDomainService followDomainService;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowDomainService followDomainService, UserRepository userRepository) {
        this.followDomainService = followDomainService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User followUserByUsername(String username, String usernameToFollow) {
        validateInputs(username, usernameToFollow);

        UserJpaEntity user = getUserEntityByUsername(username);
        UserJpaEntity userToFollow = getUserEntityByUsername(usernameToFollow);
        if (user.getId().equals(userToFollow.getId())) {
            throw new BadRequestExceptionResponse("User cannot follow himself");
        }

        return followAndSave(user, userToFollow);
    }

    @Override
    @Transactional
    public User followUserById(String username, UUID id) {
        validateInputs(username, id);

        UserJpaEntity user = getUserEntityByUsername(username);
        UserJpaEntity userToFollow = getUserEntityById(id);
        if (user.getId().equals(userToFollow.getId())) {
            throw new BadRequestExceptionResponse("User cannot follow himself");
        }
        return followAndSave(user, userToFollow);
    }

    @Override
    public User unfollowUserByUsername(String username, String usernameToUnfollow) {
        validateInputs(username, usernameToUnfollow);

        UserJpaEntity user = getUserEntityByUsername(username);
        UserJpaEntity userToUnfollow = getUserEntityByUsername(usernameToUnfollow);

        return unfollowAndSave(user, userToUnfollow);
    }

    @Override
    public User unfollowUserById(String username, UUID id) {
        validateInputs(username, id);

        UserJpaEntity user = getUserEntityByUsername(username);
        UserJpaEntity userToUnfollow = getUserEntityById(id);

        return unfollowAndSave(user, userToUnfollow);
    }

    @Override
    public FollowUserResponseList getAllFollowed(String username) {
        Set<UserJpaEntity> followed = userRepository.findFollowed(username);
        return new FollowUserResponseList(
                followed.stream().map(f -> new FollowUserResponse(
                        f.getId(),
                        f.getUsername()
                )).toList()
        );
    }

    private void validateInputs(String username, String usernameToFollow) {
        if (username == null || username.isEmpty() || usernameToFollow == null || usernameToFollow.isEmpty()) {
            throw new IllegalArgumentException("Username or Follow Name cannot be empty");
        }

        if (username.equals(usernameToFollow)) {
            throw new UsernameAlreadyExistsException(); // TODO: Use a more appropriate exception
        }

        UsernameValidator.validate(usernameToFollow);
    }

    private void validateInputs(String username, UUID id) {
        if (username == null || username.isEmpty() || id == null) {
            throw new IllegalArgumentException("Username or Follow Id cannot be empty");
        }
    }

    private UserJpaEntity getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserToFollowNotFoundException::new);
    }

    private UserJpaEntity getUserEntityById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserToFollowNotFoundException::new);
    }

    private User followAndSave(UserJpaEntity user, UserJpaEntity userToFollow) {
        User userDomain = UserEntityMapper.toDomain(user);
        User userToFollowDomain = UserEntityMapper.toDomain(userToFollow);

        followDomainService.follow(userDomain, userToFollowDomain);

        userRepository.save(UserEntityMapper.toJpaEntity(userDomain));
        userRepository.save(UserEntityMapper.toJpaEntity(userToFollowDomain));
        LOGGER.info("User Followed");
        return userToFollowDomain;
    }

    private User unfollowAndSave(UserJpaEntity user, UserJpaEntity userToUnfollow) {
        User userDomain = UserEntityMapper.toDomain(user);
        User userToUnfollowDomain = UserEntityMapper.toDomain(userToUnfollow);

        followDomainService.unfollow(userDomain, userToUnfollowDomain);

        userRepository.save(UserEntityMapper.toJpaEntity(userDomain));
        userRepository.save(UserEntityMapper.toJpaEntity(userToUnfollowDomain));
        LOGGER.info("User Unfollowed");

        return userToUnfollowDomain;
    }
}



