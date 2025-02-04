package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Account;
import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface AccountMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lastLoggedInAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    AccountEntity toEntity(Account domain);

}
