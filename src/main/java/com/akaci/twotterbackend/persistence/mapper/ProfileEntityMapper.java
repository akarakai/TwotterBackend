package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.persistence.entity.ProfileJpaEntity;

public class ProfileEntityMapper {

    public static Profile toDomain(ProfileJpaEntity profileJpaEntity) {
        String name = profileJpaEntity.getName();
        String description = profileJpaEntity.getDescription();
        return new Profile(name, description);
    }

    public static ProfileJpaEntity toJpaEntity(Profile profile) {
        String name = profile.getUsername();
        String description = profile.getDescription();
        return new ProfileJpaEntity(name, description);
    }
}
