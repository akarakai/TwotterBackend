package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.CommentRequest;
import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import com.akaci.twotterbackend.application.dto.response.CommentResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import com.akaci.twotterbackend.application.service.LikeService;
import com.akaci.twotterbackend.application.service.crud.CommentCrudService;
import com.akaci.twotterbackend.application.service.crud.TwootCrudService;
import com.akaci.twotterbackend.application.service.impl.TwootLikeService;
import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.domain.model.Twoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class TwootController {

    private static final Logger LOGGER = LogManager.getLogger(TwootController.class);

    private final TwootCrudService twootCrudService;
    private final CommentCrudService commentCrudService;
    private final LikeService twootLikeService;

    public TwootController(TwootCrudService twootCrudService, CommentCrudService commentCrudService,
                           @Qualifier("twoot-like-service") LikeService twootLikeService) {
        this.twootCrudService = twootCrudService;
        this.commentCrudService = commentCrudService;
        this.twootLikeService = twootLikeService;
    }

    // TODO SHOULD I LEAVE THIS PRIVATE OR PUBLIC? FOR NOW PRIVATE
    @GetMapping("twoot/all")
    public ResponseEntity<TwootAllResponse> getAllTwoots() {
        TwootAllResponse response = twootCrudService.getAllTwoots();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PostMapping("twoot/new")
    public ResponseEntity<TwootResponse> postNewTwoot(@RequestBody TwootRequest twootRequest) {
        String username = getAccountUsername();
        String content = twootRequest.content();
        Twoot newTwoot = twootCrudService.postNewTwoot(username, content);
        TwootResponse response = new TwootResponse(
                newTwoot.getId(),
                newTwoot.getAuthor().getUsername(),
                newTwoot.getContent(),
                0,
                0,
                newTwoot.getPostedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // Posting a new comment in a twoot is a simple crud process and there is no need
    // for domain specific logic like following/unfollowing a user
    @PostMapping("twoot/comment")
    public ResponseEntity<CommentResponse> postComment(@RequestBody CommentRequest commentRequest) {
        String username = getAccountUsername();
        String content = commentRequest.content();
        UUID twootId = commentRequest.twootId();

        Comment postedComment = commentCrudService.postNewComment(username, content, twootId);
        CommentResponse response = new CommentResponse(
                postedComment.getTwoot().getId(),
                postedComment.getId(),
                postedComment.getAuthor().getUsername(),
                postedComment.getContent(),
                0,
                postedComment.getPostedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PostMapping("twoot/{id}/like")
    public ResponseEntity<LikeResponse> likeTwoot(@PathVariable(name = "id") UUID twootId) {
        String username = getAccountUsername();
        LikeResponse response = twootLikeService.like(username, twootId);
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
