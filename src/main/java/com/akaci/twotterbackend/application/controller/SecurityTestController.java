package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.response.SignUpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class SecurityTestController {

    private static final Logger LOGGER = LogManager.getLogger(SecurityTestController.class);


    @GetMapping("test")
    public ResponseEntity<SignUpResponse> test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SignUpResponse response = new SignUpResponse(auth.getName());
        return ResponseEntity.ok(response);
    }
}
