package com.akaci.twotterbackend.security.authentication.jwt;

import com.akaci.twotterbackend.application.dto.request.SignUpRequest;
import com.akaci.twotterbackend.persistence.entity.enums.Role;
import com.akaci.twotterbackend.security.authentication.GrantedAuthorityImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JwtFilterTest {

    private static final String VALID_USERNAME = "username1998";
    private static final String VALID_PASSWORD = "password1998";
    private static final String SECURED_ENDPOINT = "/api/auth/test";
    private static final String ACCOUNT_CREATE_ENDPOINT = "/api/public/account/create";
    private static final Collection<GrantedAuthority> USER_AUTHORITY = Collections.singletonList(new GrantedAuthorityImpl(Role.USER));
    private static final String COOKIE_NAME = "jwt-token";

    @Autowired
    private MockMvc mockMvc;

    private final JwtUtil jwtUtil = new JwtUtilImpl();
    private final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() throws Exception {
        SignUpRequest request = new SignUpRequest(VALID_USERNAME, VALID_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders
                .post(ACCOUNT_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }


    @Test
    void logInWithJwt_validJwt_loggedWithSuccess() throws Exception {
        // create a jwt
        String jwt = jwtUtil.generateJwt(VALID_USERNAME, USER_AUTHORITY);
        Cookie cookie = createCookieWithJwt(jwt);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SECURED_ENDPOINT).cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(VALID_USERNAME));
        ;

    }

    @Test
    void logInWithJwt_noJwtInserted_loggedWithFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SECURED_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    private Cookie createCookieWithJwt(String jwt) {
        Cookie cookie = new Cookie(COOKIE_NAME, jwt);
        cookie.setHttpOnly(true);
        return cookie;
    }



}