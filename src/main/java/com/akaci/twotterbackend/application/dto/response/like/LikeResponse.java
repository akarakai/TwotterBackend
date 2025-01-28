package com.akaci.twotterbackend.application.dto.response.like;

// TODO MODIFY THIS RESPONSE
public record LikeResponse(
        String idContent,
        String type,
        LikeStatus likeResult
) {
}
