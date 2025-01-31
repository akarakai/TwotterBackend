package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentsOfTwootResponse;
import com.akaci.twotterbackend.application.service.crud.CommentCrudService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.commonValidator.TwootCommentValidator;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.CommentJpaEntity;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
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
        return new CommentResponse(
                twootId,
                persistedComment.getId(),
                new UserResponse(author.getId(), author.getUsername(), false),
                persistedComment.getContent(),
                0,
                persistedComment.getPostedAt(),
                false
        );
    }

    @Override
    public CommentsOfTwootResponse getCommentsOfTwoot(UUID twootId) {
        Set<CommentJpaEntity> commentEntities = commentRepository.findByTwoot(twootId);
        if (commentEntities.isEmpty()) {
            return new CommentsOfTwootResponse(
                    new ArrayList<>()
            );
        }

        return new CommentsOfTwootResponse(
                commentEntities.stream().map(commJpa ->
                        new CommentResponse(
                                commJpa.getTwoot().getId(),
                                commJpa.getId(),
                                new UserResponse(commJpa.getAuthor().getId(), commJpa.getAuthor().getUsername(), false),
                                commJpa.getContent(),
                                commJpa.getLikedByUsers().size(),
                                commJpa.getPostedAt(),
                                false

                        )).toList()
        );
    }

    @Override
    public CommentsOfTwootResponse getCommentsOfTwoot(UUID twootId, String user) {


        Set<CommentJpaEntity> commentEntities = commentRepository.findByTwoot(twootId);
        if (commentEntities.isEmpty()) {
            return new CommentsOfTwootResponse(
                    new ArrayList<>()
            );
        }

        Set<UserJpaEntity> followedByUser = userRepository.findFollowed(user);
        Set<CommentJpaEntity> commsLikedByUser = commentRepository.findLikedByUser(user);
        return new CommentsOfTwootResponse(
                commentEntities.stream().map(commJpa ->
                        new CommentResponse(
                                commJpa.getTwoot().getId(),
                                commJpa.getId(),
                                new UserResponse(commJpa.getAuthor().getId(), commJpa.getAuthor().getUsername(), isFollowedByUser(commJpa.getAuthor().getId(), followedByUser)),
                                commJpa.getContent(),
                                commJpa.getLikedByUsers().size(),
                                commJpa.getPostedAt(),
                                commsLikedByUser.contains(commJpa)

                        )).toList()
        );
    }

    private boolean isFollowedByUser(UUID authorId, Set<UserJpaEntity> followedByUser) {
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
