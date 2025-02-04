package com.akaci.twotterbackend.application.dto.response.like;

import java.util.UUID;

// TODO MODIFY THIS RESPONSE
public record LikeResponse(
        UUID idContent,
        String type,
        LikeStatus likeResult
) {
}
