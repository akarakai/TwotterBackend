package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.response.FollowUserResponse;
import com.akaci.twotterbackend.application.service.FollowService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserCrudService userCrudService;
    private final FollowService followService;

    public UserController(UserCrudService userCrudService, FollowService followService) {
        this.userCrudService = userCrudService;
        this.followService = followService;
    }

    @PostMapping("user/{username}/follow")
    public ResponseEntity<FollowUserResponse> followUser(@PathVariable(name = "username") String usernameToFollow) {
        String accountName = getAccountUsername();
        // ACCOUNT NAME == USER NAME, If you want to change displayed name, change profile name
        // username and account name cannot be changed
        User followedUser = followService.followUserByUsername(accountName, usernameToFollow);
        FollowUserResponse response = new FollowUserResponse(followedUser.getId(), followedUser.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // If the id is not UUID, spring will automatically respond with a 404
    @PostMapping("user/id/{id}/follow")
    public ResponseEntity<FollowUserResponse> followUser(@PathVariable(name = "id") UUID idToFollow) {
        String accountName = getAccountUsername();
        User followedUser = followService.followUserById(accountName, idToFollow);
        FollowUserResponse response = new FollowUserResponse(followedUser.getId(), followedUser.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("user/{username}/unfollow")
    public ResponseEntity<FollowUserResponse> unfollowUser(@PathVariable(name = "username") String usernameToUnfollow) {
        String accountName = getAccountUsername();
        User unfollowedUser = followService.unfollowUserByUsername(accountName, usernameToUnfollow);
        FollowUserResponse response = new FollowUserResponse(unfollowedUser.getId(), unfollowedUser.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("user/id/{id}/unfollow")
    public ResponseEntity<FollowUserResponse> unfollowUser(@PathVariable(name = "id") UUID idToFollow) {
        String accountName = getAccountUsername();
        User unfollowedUser = followService.unfollowUserById(accountName, idToFollow);
        FollowUserResponse response = new FollowUserResponse(unfollowedUser.getId(), unfollowedUser.getUsername());
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
