package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TwootControllerTest extends BaseAuthenticationTest {

    private static final Logger LOGGER = LogManager.getLogger(TwootControllerTest.class);
    private static final String TWOOT_CONTENT = "This is my First Twoot!11!!1";
    private static final String POST_TWOOT_ENDPOINT = "/api/twoot/new";

    @Test
    void postTwoot_postSuccess() throws Exception {
        performRequest(TWOOT_CONTENT)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(VALID_USERNAME))
                .andExpect(jsonPath("$.content").value(TWOOT_CONTENT));

    }

    @Test
    void postTwoot_InvalidContent_badRequest() throws Exception {
        performRequest("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat " +
                "cupidatat non proident.")
                .andExpect(status().isBadRequest());
    }

    private ResultActions performRequest(String content) throws Exception {
        TwootRequest request = new TwootRequest(content);
        return mockMvc.perform(MockMvcRequestBuilders
                .post(POST_TWOOT_ENDPOINT)
                .cookie(jwtDefaultUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }





}