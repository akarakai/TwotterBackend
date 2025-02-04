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
    SELECT u.followed FROM UserEntity u
    WHERE u.username = :username
    """)
    Set<UserEntity> findFollowed(String username);

//    @Query("""
//    SELECT u FROM UserJpaEntity u LEFT JOIN FETCH u.followed WHERE u.username = :username
//    """)
//    Set<UserJpaEntity> findFollowed(String username);


    @Query("""
    SELECT u FROM UserEntity u
    LEFT JOIN FETCH u.followed f
    WHERE u.username = :username
    """)
    Optional<UserEntity> findUserWithFollowed(String username);



    @Query("""
    SELECT u FROM UserEntity u
    LEFT JOIN FETCH u.likedTwoots t
    WHERE u.username = :username
    """)
    Optional<UserEntity> findUserWithTwootLiked(String username);

    @Query("""
    SELECT u FROM UserEntity u
    LEFT JOIN FETCH u.likedComments
    WHERE u.username = :username
    """)
    Optional<UserEntity> findUserWithCommentLiked(String username);



}
