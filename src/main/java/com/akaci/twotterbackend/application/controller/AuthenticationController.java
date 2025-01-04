package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.LogInRequest;
import com.akaci.twotterbackend.application.dto.response.LogInResponse;
import com.akaci.twotterbackend.application.dto.response.SignUpResponse;
import com.akaci.twotterbackend.application.service.crud.AccountCrudService;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import com.akaci.twotterbackend.security.authentication.jwt.JwtUtil;
import com.akaci.twotterbackend.security.authentication.jwt.JwtUtilImpl;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationController.class);
    private static final String COOKIE_NAME = "jwt-token";

    private final JwtUtil jwtUtil = new JwtUtilImpl();

    private final AuthenticationManager authenticationManager;
    private final AccountCrudService accountCrudService;
    private final AccountRepository accountRepository;

    public AuthenticationController(AuthenticationManager authenticationManager, AccountCrudService accountCrudService,
                                    AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.accountCrudService = accountCrudService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("test")
    public ResponseEntity<SignUpResponse> test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SignUpResponse response = new SignUpResponse(auth.getName());
        return ResponseEntity.ok(response);
    }


    // obviously this should be public because haw can you login ?
    // SEE HERE:
    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html#publish-authentication-manager-bean
    // Authenticating via rest controller is good, but you have to use an authentication manager.
    // This endpoint is also public
    @PostMapping("login")
    public ResponseEntity<LogInResponse> loginAndGetJwtToken(@RequestBody LogInRequest loginRequest, HttpServletResponse response) throws NoSuchAlgorithmException, JOSEException {
        // I don't like use this complexity in a controller. There is even checked errors. Because of the presence of JwtUtil
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication auth = this.authenticationManager.authenticate(authenticationRequest);
        String username = auth.getName();
        if (auth.isAuthenticated()) {
            accountCrudService.updateLastLoggedIn(username);
        }
        var authorities = auth.getAuthorities();
        // generate cookie
        String jwt = jwtUtil.generateJwt(username, authorities);

        Cookie cookie = new Cookie(COOKIE_NAME, jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);


        AccountJpaEntity acc = accountRepository.findByUsername(username).get();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new LogInResponse(username));
    }
}
