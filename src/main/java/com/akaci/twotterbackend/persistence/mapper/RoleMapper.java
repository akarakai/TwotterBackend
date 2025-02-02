package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.persistence.entity.RoleEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default Role toRole(RoleEntity entity) {
        return entity != null ? entity.getRole() : null;
    }

    default RoleEntity toRoleEntity(Role role) {
        return role != null ? new RoleEntity(role) : null;
    }
}