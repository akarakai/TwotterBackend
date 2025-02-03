package com.akaci.twotterbackend.application.NEWSERVICE;

import com.akaci.twotterbackend.application.dto.mapper.UserDtoMapper;
import com.akaci.twotterbackend.application.dto.response.FollowUserResponse;
import com.akaci.twotterbackend.application.dto.response.FollowUserResponseList;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.domain.model.user.User;
import com.akaci.twotterbackend.domain.model.user.UserFollow;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.mapper.UserMapper;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserDtoMapper userDtoMapper;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepo, UserDtoMapper userDtoMapper, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userDtoMapper = userDtoMapper;
        this.userMapper = userMapper;
    }

    public FollowUserResponseList getFollowed(String username) {
        Set<UserEntity> followedUsers = userRepo.findFollowed(username);
        return new FollowUserResponseList(followedUsers.stream()
                .map(userDtoMapper::toFollowedDto).toList());
    }

    @Transactional
    public FollowUserResponse follow(String username, String usernameToFollow) {
        validateUsername(usernameToFollow);
        UserEntity userToFollowEntity = userRepo.findByUsername(usernameToFollow)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found"));

        return followUser(username, userToFollowEntity);
    }

    @Transactional
    public FollowUserResponse follow(String username, UUID userToFollowId) {
        UserEntity userToFollowEntity = userRepo.findById(userToFollowId)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found"));

        return followUser(username, userToFollowEntity);
    }

    @Transactional
    public FollowUserResponse unfollow(String username, UUID userToFollowId) {
        UserEntity userToUnFollowEntity = userRepo.findById(userToFollowId)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found"));
        return unfollowUser(username, userToUnFollowEntity);
    }

    @Transactional
    public FollowUserResponse unfollow(String username, String usernameToFollow) {
        validateUsername(usernameToFollow);
        UserEntity userToUnFollowEntity = userRepo.findByUsername(usernameToFollow)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found"));
        return unfollowUser(username, userToUnFollowEntity);
    }

    private FollowUserResponse followUser(String username, UserEntity userToFollowEntity) {
        UserEntity userEntity = userRepo.findUserWithFollowed(username)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found"));

        if (userEntity.getFollowed().contains(userToFollowEntity)) {
            throw new BadRequestExceptionResponse("User is already followed");
        }

        UserFollow user = userMapper.toDomainFollow(userEntity);
        User toFollowUser = userMapper.toDomainSimple(userToFollowEntity);

        user.follow(toFollowUser);
        userEntity.getFollowed().add(userToFollowEntity);

        log.info("{} followed {}", username, userToFollowEntity.getUsername());

        return new FollowUserResponse(userToFollowEntity.getId(), userToFollowEntity.getUsername());
    }

    private FollowUserResponse unfollowUser(String username, UserEntity userToUnfollowEntity) {
        UserEntity userEntity = userRepo.findUserWithFollowed(username)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found"));

        if (!userEntity.getFollowed().contains(userToUnfollowEntity)) {
            throw new BadRequestExceptionResponse("User was not followed");
        }

        UserFollow user = userMapper.toDomainFollow(userEntity);
        User toUnfollowUser = userMapper.toDomainSimple(userToUnfollowEntity);

        user.unfollow(toUnfollowUser);
        userEntity.getFollowed().remove(userToUnfollowEntity);

        log.info("{} unfollowed {}", username, userToUnfollowEntity.getUsername());

        return new FollowUserResponse(userToUnfollowEntity.getId(), userToUnfollowEntity.getUsername());
    }



    private void validateUsername(String username) {
        UsernameValidator.validate(username);
    }

}
