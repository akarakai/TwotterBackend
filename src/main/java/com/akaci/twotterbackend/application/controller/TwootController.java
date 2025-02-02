package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.NEWSERVICE.TwootService;
import com.akaci.twotterbackend.application.dto.request.CommentRequest;
import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.comment.CommentResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import com.akaci.twotterbackend.application.service.LikeService;
import com.akaci.twotterbackend.application.service.crud.CommentCrudService;
import com.akaci.twotterbackend.application.service.crud.TwootCrudService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
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

    private final TwootService twootService;

    private final TwootCrudService twootCrudService;
    private final CommentCrudService commentCrudService;
    private final LikeService twootLikeService;

    public TwootController(TwootCrudService twootCrudService, CommentCrudService commentCrudService,
                           @Qualifier("twoot-like-service") LikeService twootLikeService,
                           TwootService twootService) {
        this.twootCrudService = twootCrudService;
        this.twootService = twootService;
        this.commentCrudService = commentCrudService;
        this.twootLikeService = twootLikeService;
    }

    @GetMapping("public/twoot")
    public ResponseEntity<TwootAllResponse> getAllTwoots(UUID twootId) {
        TwootAllResponse response = twootService.getAllTwoots();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("public/twoot/{twootId}")
    public ResponseEntity<TwootResponse> getTwoot(@PathVariable UUID twootId) {
        TwootResponse response = twootService.getTwoot(twootId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("twoot/{twootId}")
    public ResponseEntity<TwootResponse> getTwootAuth(@PathVariable UUID twootId) {
        String username = getAccountUsername();
        TwootResponse response = twootService.getTwoot(twootId, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }



    @GetMapping("twoot")
    public ResponseEntity<TwootAllResponse> getAllTwootsAuthenticated() {
        String username = getAccountUsername();
        TwootAllResponse response = twootService.getAllTwootsAuth(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PostMapping("twoot")
    public ResponseEntity<TwootResponse> postNewTwoot(@RequestBody TwootRequest twootRequest) {
        String username = getAccountUsername();
        TwootResponse response = twootService.postTwoot(twootRequest, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PostMapping("twoot/comment")
    public ResponseEntity<CommentResponse> postComment(@RequestBody CommentRequest commentRequest) {
        String username = getAccountUsername();
        String content = commentRequest.content();
        UUID twootId = commentRequest.twootId();

        CommentResponse response = commentCrudService.postNewComment(username, content, twootId);

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
