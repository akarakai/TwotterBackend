package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeStatus;
import com.akaci.twotterbackend.application.service.LikeService;
import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.service.LikeDomainService;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.CommentJpaEntity;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.repository.CommentRepository;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
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
        UserJpaEntity userEntity = getUserEntity(username);
        CommentJpaEntity twootEntity = getCommentToLike(commentId);

        User user = mapToUserModel(userEntity);
        Comment comment = mapToCommentModel(commentId);

        int initialLikeCount = userEntity.getLikedComments().size();
        likeDomainService.like(user, comment);

        if (shouldUpdateLike(userEntity, initialLikeCount)) {
            return addLike(userEntity, twootEntity);
        }

        return new LikeResponse(commentId.toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.REMOVED);

    }



    private User mapToUserModel(UserJpaEntity userEntity) {
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




    private Comment mapToCommentModel(UUID commentId) {
        return Comment.builder()
                .id(commentId)
                .build();
    }

    private boolean shouldUpdateLike(UserJpaEntity userEntity, int initialLikeCount) {
        return initialLikeCount < userEntity.getLikedComments().size() || initialLikeCount == 0;
    }

    private LikeResponse addLike(UserJpaEntity userEntity, CommentJpaEntity commentEntity) {
        userEntity.getLikedComments().add(commentEntity);
        userRepository.save(userEntity);
        return new LikeResponse(commentEntity.getId().toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.ADDED);
    }

    private UserJpaEntity getUserEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found: " + username));
    }

    private CommentJpaEntity getCommentToLike(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestExceptionResponse("Comment not found: " + commentId));
    }
}
