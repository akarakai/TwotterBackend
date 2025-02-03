package com.akaci.twotterbackend.application.dto.mapper;

import com.akaci.twotterbackend.application.dto.response.FollowUserResponse;
import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target = "isFollowed", source = "isFollowed")
    UserResponse toDto(UserEntity entity, boolean isFollowed);

    FollowUserResponse toFollowedDto(UserEntity entity);

}
