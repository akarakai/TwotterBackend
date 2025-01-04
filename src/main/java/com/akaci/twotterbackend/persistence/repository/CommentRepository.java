package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.persistence.entity.CommentJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends CrudRepository<CommentJpaEntity, UUID> {
}
