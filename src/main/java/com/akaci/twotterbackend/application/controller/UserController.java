package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.response.ProfileFollowResponse;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserCrudService userCrudService;

    public UserController(UserCrudService userCrudService) {
        this.userCrudService = userCrudService;
    }

    @PostMapping("user/{username}/follow")
    public ResponseEntity<ProfileFollowResponse> followUser(@PathVariable(name = "username") String usernameToFollow) {
        String accountName = getAccountUsername();
        // ACCOUNT NAME == USER NAME, If you want to change displayed name, change profile name
        // username and account name cannot be changed




        return null;
    }



    private String getAccountUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("could not get authentication");
        }
        return auth.getName();
    }
}
