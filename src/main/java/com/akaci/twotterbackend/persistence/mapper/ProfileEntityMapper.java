package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.persistence.entity.ProfileEntity;

public class ProfileEntityMapper {

    public static Profile toDomain(ProfileEntity profileEntity) {
        String name = profileEntity.getName();
        String description = profileEntity.getDescription();
        return new Profile(name, description);
    }

    public static ProfileEntity toJpaEntity(Profile profile) {
        String name = profile.getName();
        String description = profile.getDescription();
        return new ProfileEntity(name, description);
    }
}
