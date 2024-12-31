package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.domain.Profile;
import com.akaci.twotterbackend.domain.User;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.ProfileJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;

import java.util.UUID;

public class UserEntityMapper {

    public static User toDomain(UserJpaEntity userJpaEntity) {
        UUID id = userJpaEntity.getId();
        String username = userJpaEntity.getUsername();
        ProfileJpaEntity profileEntity = userJpaEntity.getProfile();
        AccountJpaEntity accountEntity = userJpaEntity.getAccount();

        Account account = AccountEntityMapper.toDomain(accountEntity);
        Profile profile = ProfileEntityMapper.toDomain(profileEntity);

        return new User(id, username, profile, account);
    }

    public static UserJpaEntity toJpaEntity(User user) {
        UUID id = user.getId();
        String username = user.getUsername();
        AccountJpaEntity account = AccountEntityMapper.toJpaEntity(user.getAccount());
        ProfileJpaEntity profile = ProfileEntityMapper.toJpaEntity(user.getProfile());
        return new UserJpaEntity(id, username, account, profile);
    }
}
