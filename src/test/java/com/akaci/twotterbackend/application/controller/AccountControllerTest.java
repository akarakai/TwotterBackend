package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.SignUpRequest;
import com.akaci.twotterbackend.exceptions.UsernameNotValidException;
import com.akaci.twotterbackend.persistence.entity.AccountJpaEntity;
import com.akaci.twotterbackend.persistence.entity.ProfileJpaEntity;
import com.akaci.twotterbackend.persistence.entity.RoleJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import com.akaci.twotterbackend.persistence.repository.ProfileRepository;
import com.akaci.twotterbackend.persistence.repository.RoleRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    private static final String VALID_USERNAME = "username1009";
    private static final String VALID_PASSWORD = "myBeautifulPassword1998";
    private static final String ACCOUNT_CREATE_ENDPOINT = "/api/public/account/create";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    private String toJson(Object request) throws Exception {
        return objectMapper.writeValueAsString(request);
    }

    private ResultActions performPostRequest(String endpoint, Object content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content)));
    }

    @Test
    void createAccount_success() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(VALID_USERNAME, VALID_PASSWORD);

        performPostRequest(ACCOUNT_CREATE_ENDPOINT, signUpRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(signUpRequest.username()));
    }

    @Test
    void createAccount_usernameAlreadyExist_responseConflict() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(VALID_USERNAME, VALID_PASSWORD);

        // Create account successfully
        performPostRequest(ACCOUNT_CREATE_ENDPOINT, signUpRequest);

        // Another post request with the same username
        performPostRequest(ACCOUNT_CREATE_ENDPOINT, signUpRequest)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(HttpStatus.CONFLICT.value()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {".ginoPino", "gino.Pino", "1ginoIlPino", "thisUsernameHasMoreThanTwentyCharacters",
            "_thisIsInvalid", "?badUsername", "bad@Username", "$%^&*()_+=-"})
    void createAccount_usernameNotValid_responseBadRequest(String username) throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(username, VALID_PASSWORD);

        performPostRequest(ACCOUNT_CREATE_ENDPOINT, signUpRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"short", "abcdefgh", "12345678", "Pass1"})
    void createAccount_passwordNotValid_responseBadRequest(String password) throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(VALID_USERNAME, password);

        performPostRequest(ACCOUNT_CREATE_ENDPOINT, signUpRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void createAccount_validCredentials_userCreatedSuccessfullyWithSameUsername() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(VALID_USERNAME, VALID_PASSWORD);
        performPostRequest(ACCOUNT_CREATE_ENDPOINT, signUpRequest);

        // User created with same account name
        Optional<UserJpaEntity> opUser = userRepository.findByUsername(VALID_USERNAME);
        assertTrue(opUser.isPresent());

        AccountJpaEntity account  = opUser.get().getAccount();
        assertEquals(VALID_USERNAME, account.getUsername());

        // Profile created with same account and user name. This is default behaviour
        ProfileJpaEntity profile = opUser.get().getProfile();
        assertEquals(VALID_USERNAME, profile.getName());
        assertEquals("", profile.getDescription());
    }

}
