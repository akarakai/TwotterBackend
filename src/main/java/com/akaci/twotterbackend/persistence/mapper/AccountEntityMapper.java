package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;

public class AccountEntityMapper {

    public static Account toDomain(AccountJpaEntity accountJpaEntity) {
        String username = accountJpaEntity.getUsername();
        String password = accountJpaEntity.getPassword();
        Role role = accountJpaEntity.getRole().getRole();
        return new Account(username, password, role);

    }

    public static AccountJpaEntity toJpaEntity(Account account) {
        return null;
    }
}
