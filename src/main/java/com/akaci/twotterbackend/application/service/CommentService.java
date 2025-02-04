package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.application.dto.mapper.CommentDtoMapper;
import com.akaci.twotterbackend.application.dto.mapper.UserDtoMapper;
import com.akaci.twotterbackend.application.dto.request.CommentRequest;
import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentMetadata;
import com.akaci.twotterbackend.application.dto.response.comment.CommentResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentsOfTwootResponse;
import com.akaci.twotterbackend.persistence.entity.CommentEntity;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.repository.CommentRepository;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final TwootRepository twootRepo;

    private final CommentDtoMapper commentDtoMapper;
    private final UserDtoMapper userDtoMapper;

    public CommentService(CommentRepository commentRepo, UserRepository userRepo, TwootRepository twootRepo,
                          CommentDtoMapper commentDtoMapper, UserDtoMapper userDtoMapper) {
        this.commentRepo = commentRepo;
        this.twootRepo = twootRepo;
        this.userRepo = userRepo;
        this.commentDtoMapper = commentDtoMapper;
        this.userDtoMapper = userDtoMapper;
    }

    @Transactional
    public CommentResponse postComment(CommentRequest commentReq, String username) {
        validateComment(commentReq);
        UUID twootId = commentReq.twootId();
        String content= commentReq.content();

        UserEntity authorEntity = getUserEntity(username);
        TwootEntity twootEntity = getTwootEntity(twootId);
        CommentEntity comment = CommentEntity.builder()
                .content(content)
                .author(authorEntity)
                .twoot(twootEntity).build();

        CommentEntity savedComment = commentRepo.save(comment);

        CommentMetadata metadata = new CommentMetadata();
        UserResponse author = userDtoMapper.toDto(authorEntity, false);
        return commentDtoMapper.toDto(savedComment, author, metadata);

    }

    // TODO remove code duplication
    public CommentResponse getComment(UUID commentId, String username) {
        CommentEntity commentEntity = getCommentEntity(commentId);

        UserEntity commentAuthor = commentEntity.getAuthor(); // eager

        UserResponse ur = userDtoMapper.toDto(commentAuthor, isAuthorFollowed(commentAuthor, username));
        CommentMetadata metadata = commentRepo.findCommentMetadataLikedByUser(commentId, username);

        return commentDtoMapper.toDto(commentEntity, ur, metadata);
    }

    public CommentResponse getComment(UUID commentId) {
        CommentEntity commentEntity = getCommentEntity(commentId);
        UserEntity commentAuthor = commentEntity.getAuthor(); // eager

        UserResponse ur = userDtoMapper.toDto(commentAuthor, false);
        CommentMetadata metadata = commentRepo.findCommentMetadata(commentId);

        return commentDtoMapper.toDto(commentEntity, ur, metadata);
    }



    public CommentsOfTwootResponse getComments(UUID twootId) {
        Set<CommentEntity> comments = commentRepo.findByTwoot(twootId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(commentEntity -> {
                    UserEntity commentAuthor = commentEntity.getAuthor();
                    UserResponse ur = userDtoMapper.toDto(commentAuthor, false);
                    CommentMetadata metadata = commentRepo.findCommentMetadata(commentEntity.getId());
                    return commentDtoMapper.toDto(commentEntity, ur, metadata);
                }).toList();
        return new CommentsOfTwootResponse(commentResponses);
    }

    public CommentsOfTwootResponse getComments(UUID twootId, String username) {
        UserEntity userEntity = getUserEntity(username);
        Set<CommentEntity> comments = commentRepo.findByTwoot(twootId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(commentEntity -> {
                    UserEntity commentAuthor = commentEntity.getAuthor();
                    UserResponse ur = userDtoMapper.toDto(commentAuthor, isAuthorFollowed(commentAuthor, username));
                    CommentMetadata metadata = commentRepo.findCommentMetadata(commentEntity.getId());
                    return commentDtoMapper.toDto(commentEntity, ur, metadata);
                }).toList();
        return new CommentsOfTwootResponse(commentResponses);

    }

    private UserEntity getUserEntity(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private CommentEntity getCommentEntity(UUID commentId) {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    private TwootEntity getTwootEntity(UUID twootId) {
        return twootRepo.findById(twootId)
                .orElseThrow(() -> new RuntimeException("Twoot not found"));
    }

    private boolean isAuthorFollowed(UserEntity author, String username) {
        Set<UserEntity> followedByUser = userRepo.findFollowed(username);
        return followedByUser.contains(author);
    }

    private CommentMetadata generateCommentMetadata(UUID commentId) {
        return null;
    }

    private void validateComment(CommentRequest request) {

    }


}
