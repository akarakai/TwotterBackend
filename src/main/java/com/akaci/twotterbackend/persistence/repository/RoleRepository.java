package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.RoleEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Role> {

    RoleEntity findByRole(Role role);
    List<RoleEntity> findAllByOrderByRoleAsc();
}
