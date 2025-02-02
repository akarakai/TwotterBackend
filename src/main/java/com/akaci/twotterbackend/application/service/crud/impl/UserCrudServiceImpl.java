package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.model.Account;
import com.akaci.twotterbackend.domain.model.Profile;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.mapper.UserEntityMapper;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCrudServiceImpl implements UserCrudService {

    private static final Logger LOGGER = LogManager.getLogger(UserCrudServiceImpl.class);

    private final UserRepository userRepository;

    public UserCrudServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User createUserFromAccount(User user, Account account) {
        Profile newProfileFromUser = createProfileFromUser(user);

        // insert profile and account to the user
        user.setProfile(newProfileFromUser);
//        user.setAccount(account);

        UserEntity userEntity = UserEntityMapper.toJpaEntity(user);
        userRepository.save(userEntity);
        return UserEntityMapper.toDomain(userEntity);
    }

    @Override
    public User findByAccount(Account account) {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Optional<UserEntity> optionalUserJpa = userRepository.findByUsername(username);
        if (optionalUserJpa.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }

        UserEntity userEntity = optionalUserJpa.get();
        return UserEntityMapper.toDomain(userEntity);
    }

    private Profile createProfileFromUser(User user) {
        return new Profile(user.getUsername(), null);
    }
}
