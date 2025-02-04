package com.akaci.twotterbackend.application.dto.mapper;

import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentMetadata;
import com.akaci.twotterbackend.application.dto.response.comment.CommentResponse;
import com.akaci.twotterbackend.persistence.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {

    @Mapping(target = "twootId", source = "comment.twoot.id")
    @Mapping(target = "commentId", source = "comment.id")
    CommentResponse toDto(CommentEntity comment, UserResponse author, CommentMetadata metadata);

}
