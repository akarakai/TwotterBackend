package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByUsername(String username);

}
