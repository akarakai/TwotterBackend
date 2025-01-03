package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TwootRepository extends CrudRepository<TwootJpaEntity, UUID> {




}
