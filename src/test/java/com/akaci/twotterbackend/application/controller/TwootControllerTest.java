package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.CommentRequest;
import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import com.akaci.twotterbackend.application.dto.response.like.LikeStatus;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TwootControllerTest extends BaseAuthenticationTest {

    private static final Logger LOGGER = LogManager.getLogger(TwootControllerTest.class);
    private static final String TWOOT_CONTENT = "This is my First Twoot!11!!1";
    private static final String COMMENT_CONTENT = "Nice twoot bro!";

    private static final String GET_ALL_TWOOTS_ENDPOINT = "/api/public/twoot";
    private static final String POST_TWOOT_ENDPOINT = "/api/twoot";
    private static final String POST_COMMENT_ENDPOINT = "/api/twoot/comment";

    private static final List<String> NEW_TWOOTS_CONTENT = new ArrayList<>();



    @BeforeEach
    void setUp() throws Exception {
        NEW_TWOOTS_CONTENT.add("First Twoot");
        NEW_TWOOTS_CONTENT.add("Second Twoot");
        NEW_TWOOTS_CONTENT.add("Third Twoot");
        NEW_TWOOTS_CONTENT.add("Forth Twoot");
        NEW_TWOOTS_CONTENT.add("Fifth Twoot");
    }


    private void postAllTwoots() throws Exception {
        for (String twootRequest : NEW_TWOOTS_CONTENT) {
            performPostTwootRequest(twootRequest);
        }
    }



    private ResultActions performPostTwootRequest(String content) throws Exception {
        TwootRequest request = new TwootRequest(content);
        return mockMvc.perform(MockMvcRequestBuilders
                .post(POST_TWOOT_ENDPOINT)
                .cookie(jwtDefaultUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

    private ResultActions performPostCommentRequest(UUID twootId, String content) throws Exception {
        CommentRequest commentRequest = new CommentRequest(twootId, content);
        return mockMvc.perform(MockMvcRequestBuilders
                .post(POST_COMMENT_ENDPOINT)
                .cookie(jwtDefaultUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentRequest)));
    }

    @Test
    void postTwoot_postSuccess() throws Exception {
        performPostTwootRequest(TWOOT_CONTENT)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(VALID_USERNAME))
                .andExpect(jsonPath("$.content").value(TWOOT_CONTENT));

    }

    @Test
    void postTwoot_InvalidContent_badRequest() throws Exception {
        performPostTwootRequest("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat " +
                "cupidatat non proident.")
                .andExpect(status().isBadRequest());
    }

    @Test
    void postComment_postSuccess() throws Exception {
        ResultActions ra = performPostTwootRequest(TWOOT_CONTENT);
        MockHttpServletResponse response = ra.andReturn().getResponse();
        TwootResponse twootResponse = mapper.readValue(response.getContentAsString(), TwootResponse.class);
        UUID twootId = twootResponse.id();
        performPostCommentRequest(twootId, COMMENT_CONTENT)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(VALID_USERNAME))
                .andExpect(jsonPath("$.twootId").value(twootId.toString()))
                .andExpect(jsonPath("$.content").value(COMMENT_CONTENT));
    }

    @Test
    void getAllTwoots_success() throws Exception {
        postAllTwoots();
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders
                .get(GET_ALL_TWOOTS_ENDPOINT)
                .cookie(jwtDefaultUser))
                .andExpect(status().isOk());

        TwootAllResponse response = mapper.readValue(ra.andReturn().getResponse().getContentAsString(), TwootAllResponse.class);
        assertEquals(NEW_TWOOTS_CONTENT.size(), response.totalTwootsNumber());
        assertEquals(response.lastTwootPostedAt(), response.twoots().getFirst().postedAt());
        assertEquals(NEW_TWOOTS_CONTENT.size(), response.twoots().size());
        assertTrue(response.twoots().getFirst().postedAt().isAfter(response.twoots().getLast().postedAt()));
    }

    @Test
    void likeTwoot_twootLiked() throws Exception {
        postAllTwoots();
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders
                        .get(GET_ALL_TWOOTS_ENDPOINT)
                        .cookie(jwtDefaultUser))
                .andExpect(status().isOk());
        TwootAllResponse response = mapper.readValue(ra.andReturn().getResponse().getContentAsString(), TwootAllResponse.class);

        // id twoot to like
        UUID id = response.twoots().getFirst().id();

        likeTwoot(id).andExpect(status().isOk())
                .andExpect(jsonPath("$.idContent").value(id.toString()))
                .andExpect(jsonPath("$.likeResult").value(LikeStatus.ADDED.toString()));

        // get twoots again and see if it was really liked
//        ResultActions ra2 = mockMvc.perform(MockMvcRequestBuilders
//                        .get(GET_ALL_TWOOTS_ENDPOINT)
//                        .cookie(jwtDefaultUser))
//                .andExpect(status().isOk());
//        TwootAllResponse response2 = mapper.readValue(ra.andReturn().getResponse().getContentAsString(), TwootAllResponse.class);
//        var twoot = response2.twoots().getFirst();
//        assertEquals(1, twoot.likes());


    }

    @Test
    void likeTwoot_twootAlreadyLiked_removedLike() throws Exception {
        postAllTwoots();
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders
                        .get(GET_ALL_TWOOTS_ENDPOINT)
                        .cookie(jwtDefaultUser))
                .andExpect(status().isOk());
        TwootAllResponse response = mapper.readValue(ra.andReturn().getResponse().getContentAsString(), TwootAllResponse.class);

        // id twoot to like
        UUID id = response.twoots().getFirst().id();

        likeTwoot(id).andExpect(status().isOk())
                .andExpect(jsonPath("$.idContent").value(id.toString()))
                .andExpect(jsonPath("$.likeResult").value(LikeStatus.ADDED.toString()));




    }

    private ResultActions likeTwoot(UUID twootId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post("/api/twoot/" + twootId.toString() + "/like")
                .cookie(jwtDefaultUser));
    }













}