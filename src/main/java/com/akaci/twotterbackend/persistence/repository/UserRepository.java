package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    @Query("""
    SELECT u.followed FROM UserEntity u\s
    WHERE u.username = :username
       \s
       \s""")
    Set<UserEntity> findFollowed(String username);

//    @Query("""
//    SELECT u FROM UserJpaEntity u LEFT JOIN FETCH u.followed WHERE u.username = :username
//    """)
//    Set<UserJpaEntity> findFollowed(String username);




}
