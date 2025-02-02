package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.application.dto.response.SignUpResponse;
import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StructMapperEntity {

    SignUpResponse accountToSignUpResponse(AccountEntity account);

}
