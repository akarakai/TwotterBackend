package com.akaci.twotterbackend.application.dto.mapper;

import com.akaci.twotterbackend.application.dto.response.twoot.TwootMetadata;
import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserDtoMapper.class})
public interface TwootDtoMapper {

    @Mapping(target = "id", source = "entity.id")
    TwootResponse toDto(TwootEntity entity, UserResponse author, TwootMetadata metadata);


}
