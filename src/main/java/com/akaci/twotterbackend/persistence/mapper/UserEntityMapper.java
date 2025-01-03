package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.domain.Profile;
import com.akaci.twotterbackend.domain.User;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.ProfileJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.entity.joinEntity.follow.FollowUserJpaEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserEntityMapper {

    public static User toDomain(UserJpaEntity userJpaEntity) {
        Profile profile = ProfileEntityMapper.toDomain(userJpaEntity.getProfile());

        Set<UserJpaEntity> followed = userJpaEntity.getFollowed();
        Set<UserJpaEntity> followers = userJpaEntity.getFollowers();

        // Only one level is needed
        Set<User> followedDomain = setToDomain(followed);
        Set<User> followersDomain = setToDomain(followers);

        return User.builder()
                .id(userJpaEntity.getId())
                .username(userJpaEntity.getUsername())
                .profile(profile)
                .followed(followedDomain)
                .followers(followersDomain)
                .build();
    }




    public static UserJpaEntity toJpaEntity(User user) {
        Profile profile = user.getProfile();
        ProfileJpaEntity profileJpa = ProfileEntityMapper.toJpaEntity(profile);

        Set<User> followedDomain = user.getFollowed();
        Set<User> followersDomain = user.getFollowers();

        Set<UserJpaEntity> followed = setToJpa(followedDomain);
        Set<UserJpaEntity> followers = setToJpa(followersDomain);

        return UserJpaEntity.builder()
                .id(user.getId())
                .profile(profileJpa)
                .username(user.getUsername())
                .followed(followed)
                .followers(followers)
                .build();


    }

    private static Set<UserJpaEntity> setToJpa(Set<User> user) {
        return user.stream().map(u -> {
            ProfileJpaEntity profileJpa = ProfileEntityMapper.toJpaEntity(u.getProfile());
            return UserJpaEntity.builder()
                    .id(u.getId())
                    .username(u.getUsername())
                    .profile(profileJpa)
                    .build();
        }).collect(Collectors.toSet());
    }

    // dont convert nested user (pther follower/followers)
    private static Set<User> setToDomain(Set<UserJpaEntity> user) {
        return user.stream().map(u -> {
            Profile profile = ProfileEntityMapper.toDomain(u.getProfile());
            return User.builder()
                    .id(u.getId())
                    .username(u.getUsername())
                    .profile(profile)
                    .build();
        }).collect(Collectors.toSet());
    }
}
