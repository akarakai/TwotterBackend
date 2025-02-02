package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeStatus;
import com.akaci.twotterbackend.application.service.LikeService;
import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.service.LikeDomainService;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.CommentEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.mapper.UserEntityMapper;
import com.akaci.twotterbackend.persistence.repository.CommentRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("comment-like-service")
public class CommentLikeService implements LikeService {

    private static final String CONTENT_TYPE_TO_LIKE = "comment";

    private final LikeDomainService likeDomainService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentLikeService(UserRepository userRepository, CommentRepository commentRepository,
                              @Qualifier("comment-like-domain-service") LikeDomainService likeDomainService) {
        this.userRepository = userRepository;
        this.likeDomainService = likeDomainService;
        this.commentRepository = commentRepository;
    }


    @Override
    public LikeResponse like(String username, UUID commentId) {
        UserEntity userEntity = getUserEntity(username);
        CommentEntity commentEntity = getCommentToLike(commentId);

        User user = mapToUserModel(userEntity);
        Comment comment = mapToCommentModel(commentEntity);

        int initialCommentLikes = comment.getLikedByUsers().size();

        likeDomainService.like(user, comment);

        if (shouldUpdateLike(comment, initialCommentLikes)) {
            return addLike(userEntity, commentEntity);
        }

        return removeLike(userEntity, commentEntity);
    }



    private User mapToUserModel(UserEntity userEntity) {
        Set<Comment> likedComments = userEntity.getLikedComments().stream()
                .map(t -> Comment.builder()
                        .id(t.getId())
                        .build())
                .collect(Collectors.toSet());

        return User.builder()
                .id(userEntity.getId())
                .likedComments(likedComments)
                .build();
    }


    private Comment mapToCommentModel(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .likedByUsers(UserEntityMapper.setToDomain(commentEntity.getLikedByUsers()))
                .build();
    }

    private boolean shouldUpdateLike(Comment comment, int initialLikeCount) {
        return initialLikeCount < comment.getLikedByUsers().size() || initialLikeCount == 0;
    }

    private LikeResponse addLike(UserEntity userEntity, CommentEntity commentEntity) {
        userEntity.getLikedComments().add(commentEntity);
        userRepository.save(userEntity);
        return new LikeResponse(commentEntity.getId().toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.ADDED);
    }

    private LikeResponse removeLike(UserEntity userEntity, CommentEntity commentEntity) {
        userEntity.getLikedComments().remove(commentEntity);
        userRepository.save(userEntity);
        return new LikeResponse(commentEntity.getId().toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.REMOVED);
    }

    private UserEntity getUserEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found: " + username));
    }

    private CommentEntity getCommentToLike(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestExceptionResponse("Comment not found: " + commentId));
    }
}
