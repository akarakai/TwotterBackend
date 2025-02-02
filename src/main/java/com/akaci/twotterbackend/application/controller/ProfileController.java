package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.NEWSERVICE.ProfileService;
import com.akaci.twotterbackend.application.dto.request.UpdateProfileDescriptionRequest;
import com.akaci.twotterbackend.application.dto.response.ProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("profile")
    public ResponseEntity<ProfileResponse> getProfile()  {
        String username = getAccountUsername();
        ProfileResponse response = profileService.getProfile(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("profile/description")
    public ResponseEntity<ProfileResponse> updateProfileDescription(@RequestBody UpdateProfileDescriptionRequest descriptionRequest) {
        String newDescription = descriptionRequest.newDescription();
        String username = getAccountUsername();
        ProfileResponse response = profileService.updateDescription(username, newDescription);
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
