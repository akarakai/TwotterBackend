package com.akaci.twotterbackend.application.dto.response;

import java.util.UUID;

public record FollowUserResponse(
        UUID id,
        String username
) {
}
