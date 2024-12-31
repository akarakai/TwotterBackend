package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.domain.Account;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountJpaEntity, Long> {


    Optional<AccountJpaEntity> findByUsername(String username);
}
