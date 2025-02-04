package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.NEWSERVICE.CommentService;
import com.akaci.twotterbackend.application.NEWSERVICE.like.LikeContentService;
import com.akaci.twotterbackend.application.dto.request.CommentRequest;
import com.akaci.twotterbackend.application.dto.response.comment.CommentResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentsOfTwootResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api")
public class CommentController {

    private final CommentService commentService;
    private final LikeContentService commentLikeService;


    public CommentController(CommentService commentService,
                             @Qualifier("comment") LikeContentService commentLikeService) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;

    }

    @GetMapping("comment")
    public ResponseEntity<CommentsOfTwootResponse> getCommentsOfTwoot(@RequestParam("twoot_id") UUID twootId) {
        String username = getAccountUsername();
        CommentsOfTwootResponse response = commentService.getComments(twootId, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("public/comment")
    public ResponseEntity<CommentsOfTwootResponse> getCommentsOfTwootAuth(@RequestParam("twoot_id") UUID twootId) {
        CommentsOfTwootResponse response = commentService.getComments(twootId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("comment/{id}")
    public ResponseEntity<CommentResponse> getCommentAuth(@PathVariable UUID id) {
        String username = getAccountUsername();
        CommentResponse response = commentService.getComment(id, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("public/comment/{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable UUID id) {
        CommentResponse response = commentService.getComment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("twoot/comment")
    public ResponseEntity<CommentResponse> postComment(@RequestBody CommentRequest commentRequest) {
        String username = getAccountUsername();
        CommentResponse response = commentService.postComment(commentRequest, username);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("comment/{id}/like")
    public ResponseEntity<LikeResponse> likeComment(@PathVariable("id") UUID commentId) {
        String username = getAccountUsername();
        LikeResponse response = commentLikeService.addLike(commentId, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("comment/{id}/unlike")
    public ResponseEntity<LikeResponse> unlikeComment(@PathVariable("id") UUID commentId) {
        String username = getAccountUsername();
        LikeResponse response = commentLikeService.removeLike(commentId, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private String getAccountUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("could not get authentication");
        }
        return auth.getName();
    }

}
