package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Account;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import com.akaci.twotterbackend.persistence.entity.RoleEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;

import java.util.UUID;

public class AccountEntityMapper {

    public static Account toDomain(AccountEntity accountEntity) {
        UUID id = accountEntity.getId();
        String username = accountEntity.getUsername();
        String password = accountEntity.getPassword();
        Role role = accountEntity.getRole().getRole();
        User user = UserEntityMapper.toDomain(accountEntity.getUser());
        return Account.builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

    public static AccountEntity toJpaEntity(Account account) {
        return null;

    }
}
