package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.RoleJpaEntity;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<RoleJpaEntity, Role> {

    RoleJpaEntity findByRole(Role role);
    List<RoleJpaEntity> findAllByOrderByRoleAsc();
}
