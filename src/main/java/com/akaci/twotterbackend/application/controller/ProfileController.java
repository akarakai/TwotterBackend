package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.ProfileRequest;
import com.akaci.twotterbackend.application.dto.request.UpdateProfileDescriptionRequest;
import com.akaci.twotterbackend.application.dto.response.ProfileResponse;
import com.akaci.twotterbackend.application.service.crud.ProfileCrudService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.Profile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("api")
public class ProfileController {

    private static final Logger lOGGER = LogManager.getLogger(ProfileController.class);

    private final ProfileCrudService profileCrudService;

    public ProfileController(ProfileCrudService profileCrudService) {
        this.profileCrudService = profileCrudService;
    }



    // No request body needed because you get username after authentication
    @GetMapping("profile")
    public ResponseEntity<ProfileResponse> getProfile()  {
        String accountName = getAccountUsername();
        Profile profile = profileCrudService.getProfileFromUsername(accountName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body( new ProfileResponse(profile.getUsername(), profile.getDescription()));
    }

    @PutMapping("profile/description")
    public ResponseEntity<ProfileResponse> updateProfileDescription(@RequestBody UpdateProfileDescriptionRequest descriptionRequest) {
        String newDescription = descriptionRequest.newDescription();
        String accountName = getAccountUsername();
        Profile updatedProfile = profileCrudService.updateProfileDescription(accountName, newDescription);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProfileResponse(updatedProfile.getUsername(), updatedProfile.getDescription()));

    }




    private String getAccountUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("could not get authentication");
        }
        return auth.getName();
    }





}
