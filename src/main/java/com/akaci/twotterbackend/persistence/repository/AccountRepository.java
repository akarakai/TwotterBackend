package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByUsername(String username);

    // https://stackoverflow.com/questions/43665090/why-do-we-have-to-use-modifying-annotation-for-queries-in-data-jpa
    @Modifying(clearAutomatically = true, flushAutomatically = true)   // otherwise test fails
    @Query("""
    UPDATE AccountEntity a
    SET a.lastLoggedInAt = :time
    WHERE a.username = :username
    """)
    void updateLastLogIn(String username, LocalDateTime time);

}
