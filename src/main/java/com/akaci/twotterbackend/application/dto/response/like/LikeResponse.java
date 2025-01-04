package com.akaci.twotterbackend.application.dto.response.like;

import java.util.UUID;

// TODO MODIFY THIS RESPONSE
public record LikeResponse(
        String idContent,
        String contentType,
        String action
) {
}
