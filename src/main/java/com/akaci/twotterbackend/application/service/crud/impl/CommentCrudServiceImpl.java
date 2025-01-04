package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.service.crud.CommentCrudService;
import com.akaci.twotterbackend.domain.commonValidator.TwootCommentValidator;
import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.CommentJpaEntity;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.mapper.CommentEntityMapper;
import com.akaci.twotterbackend.persistence.repository.CommentRepository;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CommentCrudServiceImpl implements CommentCrudService {

    private static final Logger LOGGER = LogManager.getLogger(CommentCrudServiceImpl.class);

    // TODO USE ALREADY EXISTENT USER CRUD SERVICE, BUT USELESS MAPPING NEEDED
    // BECAUSE IT RETURNS DOMAIN. MAYBE THE RETURN IS WRONG?
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TwootRepository twootRepository;

    public CommentCrudServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
                                  TwootRepository twootRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.twootRepository = twootRepository;
    }

    @Override
    @Transactional
    public Comment postNewComment(String username, String content, UUID twootId) {
        validateParameters(content);
        UserJpaEntity author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        TwootJpaEntity twoot = twootRepository.findById(twootId)
                .orElseThrow(() -> new BadRequestExceptionResponse("twoot not found"));

        // because twoot and author are inside comment and they contain the
        // foreign key (they are the boss of the relation), there is not need to
        // put the comment inside the twoot and author sets in order to persist in the database
        CommentJpaEntity comment = CommentJpaEntity
                .builder()
                .content(content)
                .author(author)
                .twoot(twoot)
                .postedAt(LocalDateTime.now())
                .build();

        CommentJpaEntity persistedComment = commentRepository.save(comment);
        return CommentEntityMapper.toDomain(persistedComment);

    }





        private void validateParameters(String content) {
        try {
            TwootCommentValidator.validateCommentContent(content);
        } catch (IllegalArgumentException e) {
            throw new BadRequestExceptionResponse(e.getMessage());
        }
    }



}
