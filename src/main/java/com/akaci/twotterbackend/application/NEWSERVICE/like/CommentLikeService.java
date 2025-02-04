package com.akaci.twotterbackend.application.NEWSERVICE.like;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeStatus;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.CommentEntity;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.repository.CommentRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service("comment")
public class CommentLikeService implements LikeContentService {

    private final static String LIKE_TYPE = "comment";

    private final CommentRepository commentRepo;
    private final UserRepository userRepo;

    public CommentLikeService(CommentRepository commentRepo, UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public LikeResponse addLike(UUID commentId, String username) {
        UserEntity userEntity = getUserAlongWithLikedComments(username);
        CommentEntity commentEntity = getComment(commentId);

        Set<CommentEntity> likedComments = userEntity.getLikedComments();

        if (likedComments.contains(commentEntity)) throw new BadRequestExceptionResponse("Comment already liked");

        likedComments.add(commentEntity);
        userEntity.setLikedComments(likedComments);

        return new LikeResponse(commentId, LIKE_TYPE, LikeStatus.ADDED);
    }

    @Override
    @Transactional
    public LikeResponse removeLike(UUID commentId, String username) {
        UserEntity userEntity = getUserAlongWithLikedComments(username);
        CommentEntity commentEntity = getComment(commentId);

        Set<CommentEntity> likedComments = userEntity.getLikedComments();

        if (!likedComments.contains(commentEntity)) throw new BadRequestExceptionResponse("Comment was not already liked");

        likedComments.remove(commentEntity);
        userEntity.setLikedComments(likedComments);

        return new LikeResponse(commentId, LIKE_TYPE, LikeStatus.REMOVED);
    }

    private UserEntity getUserAlongWithLikedComments(String username) {
        return userRepo.findUserWithCommentLiked(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private CommentEntity getComment(UUID commentId) {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Twoot not found"));
    }
}
