package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.RoleJpaEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;

import java.util.UUID;

public class AccountEntityMapper {

    public static Account toDomain(AccountJpaEntity accountJpaEntity) {
        UUID id = accountJpaEntity.getId();
        String username = accountJpaEntity.getUsername();
        String password = accountJpaEntity.getPassword();
        Role role = accountJpaEntity.getRole().getRole();
        return new Account(id, username, password, role);

    }

    public static AccountJpaEntity toJpaEntity(Account account) {
        UUID id = account.getId();
        String username = account.getUsername();
        String password = account.getPassword();
        Role role = account.getRole();
        return new AccountJpaEntity(id, username, password, role);
    }
}
