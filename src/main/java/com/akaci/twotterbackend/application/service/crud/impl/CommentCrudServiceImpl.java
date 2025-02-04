package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentsOfTwootResponse;
import com.akaci.twotterbackend.application.service.crud.CommentCrudService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.commonValidator.TwootCommentValidator;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.CommentEntity;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.repository.CommentRepository;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentCrudServiceImpl implements CommentCrudService {

    private static final Logger LOGGER = LogManager.getLogger(CommentCrudServiceImpl.class);

    // TODO USE ALREADY EXISTENT USER CRUD SERVICE, BUT USELESS MAPPING NEEDED
    // BECAUSE IT RETURNS DOMAIN. MAYBE THE RETURN IS WRONG?
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TwootRepository twootRepository;
    private final UserCrudService userCrudService;

    public CommentCrudServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
                                  TwootRepository twootRepository, UserCrudService userCrudService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.twootRepository = twootRepository;
        this.userCrudService = userCrudService;
    }

    @Override
    @Transactional
    public CommentResponse postNewComment(String username, String content, UUID twootId) {
        validateParameters(content);
        UserEntity author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        TwootEntity twoot = twootRepository.findById(twootId)
                .orElseThrow(() -> new BadRequestExceptionResponse("twoot not found"));

        // because twoot and author are inside comment and they contain the
        // foreign key (they are the boss of the relation), there is not need to
        // put the comment inside the twoot and author sets in order to persist in the database
        CommentEntity comment = CommentEntity
                .builder()
                .content(content)
                .author(author)
                .twoot(twoot)
                .postedAt(LocalDateTime.now())
                .build();

        CommentEntity persistedComment = commentRepository.save(comment);
        return null;
    }

    @Override
    public CommentsOfTwootResponse getCommentsOfTwoot(UUID twootId) {
        Set<CommentEntity> commentEntities = commentRepository.findByTwoot(twootId);
        if (commentEntities.isEmpty()) {
            return new CommentsOfTwootResponse(
                    new ArrayList<>()
            );
        }

        return null;
    }

    @Override
    public CommentsOfTwootResponse getCommentsOfTwoot(UUID twootId, String user) {


        Set<CommentEntity> commentEntities = commentRepository.findByTwoot(twootId);
        if (commentEntities.isEmpty()) {
            return new CommentsOfTwootResponse(
                    new ArrayList<>()
            );
        }

        Set<UserEntity> followedByUser = userRepository.findFollowed(user);
        Set<CommentEntity> commsLikedByUser = commentRepository.findLikedByUser(user);
        return null;
    }

    private boolean isFollowedByUser(UUID authorId, Set<UserEntity> followedByUser) {
        return followedByUser.stream().anyMatch(follw -> follw.getId().equals(authorId));
    }




    private void validateParameters(String content) {
        try {
            TwootCommentValidator.validateCommentContent(content);
        } catch (IllegalArgumentException e) {
            throw new BadRequestExceptionResponse(e.getMessage());
        }
    }



}
