package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.service.AuthenticationService;
import com.akaci.twotterbackend.application.dto.request.LogInRequest;
import com.akaci.twotterbackend.application.dto.response.LogInResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<LogInResponse> loginAndGetJwtToken(@RequestBody LogInRequest loginRequest, HttpServletResponse response) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        LogInResponse loginResponse = authService.login(username, password, response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginResponse);
    }
}
