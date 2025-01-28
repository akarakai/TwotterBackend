package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.SignUpRequest;
import com.akaci.twotterbackend.application.dto.response.SignUpResponse;
import com.akaci.twotterbackend.application.service.AuthenticationService;
import com.akaci.twotterbackend.domain.model.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {

    private static final Logger LOGGER = LogManager.getLogger(AccountController.class);
    private final AuthenticationService authenticationService;

    public AccountController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("public/account")
    public ResponseEntity<SignUpResponse> createAccount(@RequestBody SignUpRequest signUpRequest) {
        String username = signUpRequest.username();
        String password = signUpRequest.password();

        Account newAccount = authenticationService.signUp(username, password);
        SignUpResponse accountResponse = new SignUpResponse(newAccount.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountResponse);
    }

}
