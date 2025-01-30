package com.akaci.twotterbackend.application.dto.response.comment;

import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;

import java.util.List;

public record CommentsOfTwootResponse (
        List<CommentResponse> comments

) {
}
