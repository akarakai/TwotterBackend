package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.ProfileCrudService;
import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.persistence.entity.ProfileJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.mapper.ProfileEntityMapper;
import com.akaci.twotterbackend.persistence.repository.ProfileRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileCrudServiceImpl implements ProfileCrudService {

    private static final Logger LOGGER = LogManager.getLogger(ProfileCrudServiceImpl.class);

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;


    public ProfileCrudServiceImpl(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile getProfileFromUsername(String username) {
        ProfileJpaEntity profileJpaEntity = getProfileJpaEntity(username);

        return ProfileEntityMapper.toDomain(profileJpaEntity);
    }

    @Override
    public Profile updateProfileDescription(String username, String newDescription) {
        // TODO THis is bad, I created profile only because there is a validator in the description
        Profile profile = new Profile(username, newDescription);

        ProfileJpaEntity profileJpaEntity = getProfileJpaEntity(profile.getUsername());

        profileJpaEntity.setDescription(newDescription);


        // save
        ProfileJpaEntity savedNewProfile = profileRepository.save(profileJpaEntity);

        return ProfileEntityMapper.toDomain(savedNewProfile);
    }


    private ProfileJpaEntity getProfileJpaEntity(String username) {
        Optional<UserJpaEntity> opUserJpa = userRepository.findByUsername(username);
        if (opUserJpa.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }

        UserJpaEntity userJpaEntity = opUserJpa.get();
        return userJpaEntity.getProfile();
    }
}
