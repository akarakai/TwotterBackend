package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.persistence.entity.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toDomain(ProfileEntity entity);
    ProfileEntity toEntity(Profile profile);
}
