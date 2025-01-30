package com.akaci.twotterbackend.application.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        boolean isFollowed
) {
}
