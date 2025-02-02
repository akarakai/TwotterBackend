package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.ProfileCrudService;
import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.persistence.entity.ProfileEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
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
        ProfileEntity profileEntity = getProfileJpaEntity(username);

        return ProfileEntityMapper.toDomain(profileEntity);
    }

    @Override
    public Profile updateProfileDescription(String username, String newDescription) {
        // TODO THis is bad, I created profile only because there is a validator in the description
        Profile profile = new Profile(username, newDescription);

        ProfileEntity profileEntity = getProfileJpaEntity(profile.getName());

        profileEntity.setDescription(newDescription);


        // save
        ProfileEntity savedNewProfile = profileRepository.save(profileEntity);

        return ProfileEntityMapper.toDomain(savedNewProfile);
    }


    private ProfileEntity getProfileJpaEntity(String username) {
        Optional<UserEntity> opUserJpa = userRepository.findByUsername(username);
        if (opUserJpa.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }

        UserEntity userEntity = opUserJpa.get();
        return userEntity.getProfile();
    }
}
