package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<AccountJpaEntity, UUID> {

    Optional<AccountJpaEntity> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE AccountJpaEntity a SET a.lastLoggedInAt = :lastLogin WHERE a.username = :username")
    void updateLastLogin(String username, LocalDateTime lastLogin);
}
