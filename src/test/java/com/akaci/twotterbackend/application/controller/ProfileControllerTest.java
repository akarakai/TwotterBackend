package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.UpdateProfileDescriptionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileControllerTest extends BaseAuthenticationTest {

    private static final String GET_PROFILE_ENDPOINT = "/api/profile";
    private static final String PUT_PROFILE_NEW_DESCRIPTION = "/api/profile/description";
    private static final String DEFAULT_EMPtY_DESCRIPTION = "";

    private static final String VALID_NEW_DESCRIPTION = "this is a new description";
    private static final String TOO_LONG_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
            "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit.";

    @Test
    void getProfile_successful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(GET_PROFILE_ENDPOINT)
                        .cookie(jwtDefaultUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileName").value(VALID_USERNAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_EMPtY_DESCRIPTION));
    }

    @Test
    void modifyDescription_successful() throws Exception {
        stockMvcRequest(VALID_NEW_DESCRIPTION)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(VALID_NEW_DESCRIPTION));

        // control new profile and check if the new description is present
        mockMvc.perform(MockMvcRequestBuilders
                        .get(GET_PROFILE_ENDPOINT)
                        .cookie(jwtDefaultUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileName").value(VALID_USERNAME))
                .andExpect(jsonPath("$.description").value(VALID_NEW_DESCRIPTION));
    }

    @Test
    void modifyDescription_tooBigDescription_badRequestResponse() throws Exception {
        stockMvcRequest(TOO_LONG_DESCRIPTION)
        .andExpect(status().isBadRequest());
    }


    private ResultActions stockMvcRequest(String description) throws Exception {
        UpdateProfileDescriptionRequest request = new UpdateProfileDescriptionRequest(description);
        return mockMvc.perform(MockMvcRequestBuilders
                .put(PUT_PROFILE_NEW_DESCRIPTION)
                .cookie(jwtDefaultUser)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(request)));

    }
}