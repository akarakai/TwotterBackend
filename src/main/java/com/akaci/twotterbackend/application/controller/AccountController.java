package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.security.authentication.AuthenticationService;
import com.akaci.twotterbackend.application.dto.request.SignUpRequest;
import com.akaci.twotterbackend.application.dto.response.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AuthenticationService authService;

    public AccountController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("public/account")
    public ResponseEntity<SignUpResponse> createAccount(@RequestBody SignUpRequest signUpRequest) {
        String username = signUpRequest.username();
        String password = signUpRequest.password();

        SignUpResponse response = authService.createAccount(username, password);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
