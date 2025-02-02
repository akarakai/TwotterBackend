package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.persistence.entity.ProfileEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserEntityMapper {

    public static User toDomain(UserEntity userEntity) {
        Profile profile = ProfileEntityMapper.toDomain(userEntity.getProfile());

        Set<UserEntity> followed = userEntity.getFollowed();
        Set<UserEntity> followers = userEntity.getFollowers();
        if (followed == null) {
            followed = new HashSet<>();
        }
        if (followers == null) {
            followers = new HashSet<>();
        }

        // Only one level is needed
        Set<User> followedDomain = setToDomain(followed);
        Set<User> followersDomain = setToDomain(followers);

        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .profile(profile)
                .followed(followedDomain)
                .followers(followersDomain)
                .build();
    }




    public static UserEntity toJpaEntity(User user) {
        Profile profile = user.getProfile();
        ProfileEntity profileJpa = ProfileEntityMapper.toJpaEntity(profile);

        Set<User> followedDomain = user.getFollowed();
        Set<User> followersDomain = user.getFollowers();

        Set<UserEntity> followed = setToJpa(followedDomain);
        Set<UserEntity> followers = setToJpa(followersDomain);

        return UserEntity.builder()
                .id(user.getId())
                .profile(profileJpa)
                .username(user.getUsername())
                .followed(followed)
                .followers(followers)
                .build();


    }

    public static Set<UserEntity> setToJpa(Set<User> user) {
        return user.stream().map(u -> {
            ProfileEntity profileJpa = ProfileEntityMapper.toJpaEntity(u.getProfile());
            return UserEntity.builder()
                    .id(u.getId())
                    .username(u.getUsername())
                    .profile(profileJpa)
                    .build();
        }).collect(Collectors.toSet());
    }

    // dont convert nested user (pther follower/followers)
    public static Set<User> setToDomain(Set<UserEntity> user) {
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
