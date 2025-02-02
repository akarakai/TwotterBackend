package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.ProfileEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, UUID> {

    @Query("""
    SELECT p FROM ProfileEntity p
    WHERE p.name = :username
    """)
    Optional<ProfileEntity> findByUsername(String username);

    @Modifying
    @Query("""
    UPDATE ProfileEntity p
    SET p.description = :description
    WHERE p.name = :username
    """)
    int setDescription(String username, String description);

}
