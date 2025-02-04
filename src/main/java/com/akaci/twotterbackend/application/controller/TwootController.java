package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.NEWSERVICE.TwootService;
import com.akaci.twotterbackend.application.NEWSERVICE.like.LikeContentService;
import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
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
    private final LikeContentService twootLikeService;

    public TwootController(TwootService twootService,
                           @Qualifier("twoot") LikeContentService twootLikeService) {
        this.twootLikeService = twootLikeService;
        this.twootService = twootService;
    }

    @GetMapping("public/twoot")
    public ResponseEntity<TwootAllResponse> getAllTwoots() {
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


    @PostMapping("twoot/{id}/like")
    public ResponseEntity<LikeResponse> likeTwoot(@PathVariable(name = "id") UUID twootId) {
        String username = getAccountUsername();
        LikeResponse response = twootLikeService.addLike(twootId, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("twoot/{id}/unlike")
    public ResponseEntity<LikeResponse> removeLikeTwoot(@PathVariable(name = "id") UUID twootId) {
        String username = getAccountUsername();
        LikeResponse response = twootLikeService.removeLike(twootId, username);
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
