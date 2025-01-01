package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.ProfileCrudService;
import com.akaci.twotterbackend.domain.Profile;
import com.akaci.twotterbackend.domain.User;
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


    public ProfileCrudServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Profile getProfileFromUsername(String username) {
        Optional<UserJpaEntity> opUserJpa = userRepository.findByUsername(username);
        if (opUserJpa.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }

        UserJpaEntity userJpaEntity = opUserJpa.get();
        ProfileJpaEntity profileJpaEntity = userJpaEntity.getProfile();

        return ProfileEntityMapper.toDomain(profileJpaEntity);


    }
}
