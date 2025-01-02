package com.akaci.twotterbackend.application.dto.response;

import java.util.UUID;

public record ProfileFollowResponse(
        UUID id,
        String username
) {
}
