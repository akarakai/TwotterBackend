package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.user.User;
import com.akaci.twotterbackend.domain.model.user.UserFollow;
import com.akaci.twotterbackend.domain.model.user.UserLike;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {ProfileMapper.class})

public interface UserMapper {

    User toDomainSimple(UserEntity entity);
    UserLike toDomainLike(UserEntity entity);
    UserFollow toDomainFollow(UserEntity entity);


    @Mapping(target = "twoots", ignore = true)
    @Mapping(target = "likedTwoots", ignore = true)
    @Mapping(target = "likedComments", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "followed", ignore = true)
    @Mapping(target = "comments", ignore = true)
    UserEntity toEntity(User user);


    @Mapping(target = "username", ignore = true)
    @Mapping(target = "twoots", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "followed", ignore = true)
    @Mapping(target = "comments", ignore = true)
    UserEntity toEntity(UserLike like);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "twoots", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "likedTwoots", ignore = true)
    @Mapping(target = "likedComments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    UserEntity toEntity(UserFollow follow);



}
