package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Account;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.RoleJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;

import java.util.UUID;

public class AccountEntityMapper {

    public static Account toDomain(AccountJpaEntity accountJpaEntity) {
        UUID id = accountJpaEntity.getId();
        String username = accountJpaEntity.getUsername();
        String password = accountJpaEntity.getPassword();
        Role role = accountJpaEntity.getRole().getRole();
        User user = UserEntityMapper.toDomain(accountJpaEntity.getUser());
        return Account.builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .user(user)
                .build();
    }

    public static AccountJpaEntity toJpaEntity(Account account) {
        UUID id = account.getId();
        UserJpaEntity userJpaEntity = UserEntityMapper.toJpaEntity(account.getUser());
        RoleJpaEntity roleJpaEntity = new RoleJpaEntity(account.getRole());
        String username = account.getUsername();
        String password = account.getPassword();
        return AccountJpaEntity.builder()
                .id(id)
                .user(userJpaEntity)
                .role(roleJpaEntity)
                .username(username)
                .password(password)
                .build();

    }
}
