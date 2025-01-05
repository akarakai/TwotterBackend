package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.CommentRequest;
import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import com.akaci.twotterbackend.application.dto.response.CommentResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeStatus;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends BaseAuthenticationTest {

    private static final Logger LOGGER = LogManager.getLogger(TwootControllerTest.class);

    private static final String POST_TWOOT_ENDPOINT = "/api/twoot/new";
    private static final String POST_COMMENT_ENDPOINT = "/api/twoot/comment";
    private static final String CONTENT_TYPE_TO_LIKE = "comment";

    private UUID commentId;


    @BeforeEach
    void setUp() throws Exception {
        // post twoot and get his id
        ResultActions ra = performPostTwootRequest("This is a twoot");
        TwootResponse tResp = mapper.readValue(ra.andReturn().getResponse().getContentAsString(), TwootResponse.class);

        // comment the twoot and get the comment id
        ResultActions raComm = performPostCommentRequest(tResp.id(), "This is a comment");
        CommentResponse cResp = mapper.readValue(raComm.andReturn().getResponse().getContentAsString(), CommentResponse.class);
        commentId = cResp.commentId();

        LOGGER.info("Comment Id: {}", commentId);
    }

    private ResultActions performPostCommentRequest(UUID twootId, String content) throws Exception {
        CommentRequest commentRequest = new CommentRequest(twootId, content);
        return mockMvc.perform(MockMvcRequestBuilders
                .post(POST_COMMENT_ENDPOINT)
                .cookie(jwtDefaultUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void likeComment_commentWasNotLiked_commentIsLiked() throws Exception {
        ResultActions ra = performLikeComment(commentId)
                .andExpect(status().isOk());
        LikeResponse lr = mapper.readValue(ra.andReturn().getResponse().getContentAsString(), LikeResponse.class);

        assertEquals(commentId.toString(), lr.idContent());
        assertEquals(LikeStatus.ADDED, lr.likeResult());
        assertEquals(CONTENT_TYPE_TO_LIKE, lr.contentType());
    }

    @Test
    void likeComment_commentWasLiked_commentIsNotLIkedAnymore() throws Exception {
        ResultActions ra1 = performLikeComment(commentId);
        ResultActions ra = performLikeComment(commentId)
                .andExpect(status().isOk());
        LikeResponse lr = mapper.readValue(ra.andReturn().getResponse().getContentAsString(), LikeResponse.class);
        assertEquals(commentId.toString(), lr.idContent());
        assertEquals(LikeStatus.REMOVED, lr.likeResult());
        assertEquals(CONTENT_TYPE_TO_LIKE, lr.contentType());
    }



    private ResultActions performLikeComment(UUID commentId) throws Exception {
        String url = "/api/comment/" + commentId.toString() + "/like";
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .cookie(jwtDefaultUser));
    }


    private ResultActions performPostTwootRequest(String content) throws Exception {
        TwootRequest request = new TwootRequest(content);
        return mockMvc.perform(MockMvcRequestBuilders
                .post(POST_TWOOT_ENDPOINT)
                .cookie(jwtDefaultUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

}