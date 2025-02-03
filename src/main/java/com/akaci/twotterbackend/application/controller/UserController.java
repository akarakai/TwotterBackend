package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.NEWSERVICE.UserService;
import com.akaci.twotterbackend.application.dto.response.FollowUserResponse;
import com.akaci.twotterbackend.application.dto.response.FollowUserResponseList;
import com.akaci.twotterbackend.application.service.FollowService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api")
public class UserController {

    private final FollowService followService;

    private final UserService userService;

    public UserController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @GetMapping("user/followed")
    public ResponseEntity<FollowUserResponseList> getFollowed() {
        String accountName = getAccountUsername();
        FollowUserResponseList response = userService.getFollowed(accountName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);

    }

    @PostMapping("user/{username}/follow")
    public ResponseEntity<FollowUserResponse> followUser(@PathVariable(name = "username") String usernameToFollow) {
        String username = getAccountUsername();
        FollowUserResponse response = userService.follow(username, usernameToFollow);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // If the id is not UUID, spring will automatically respond with a 404
    @PostMapping("user/id/{id}/follow")
    public ResponseEntity<FollowUserResponse> followUser(@PathVariable(name = "id") UUID idToFollow) {
        String username = getAccountUsername();
        FollowUserResponse response = userService.follow(username, idToFollow);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("user/{username}/unfollow")
    public ResponseEntity<FollowUserResponse> unfollowUser(@PathVariable(name = "username") String usernameToUnfollow) {
        String username = getAccountUsername();
        FollowUserResponse response = userService.unfollow(username, usernameToUnfollow);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("user/id/{id}/unfollow")
    public ResponseEntity<FollowUserResponse> unfollowUser(@PathVariable(name = "id") UUID idToFollow) {
        String username = getAccountUsername();
        FollowUserResponse response = userService.unfollow(username, idToFollow);
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
