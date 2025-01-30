package com.akaci.twotterbackend.application.dto.response.twoot;

import com.akaci.twotterbackend.application.dto.response.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record TwootResponse(
        UUID id,
        UserResponse author,
        String content,
        int likes,
        int commentNumber,
        LocalDateTime postedAt,
        boolean likedByUser
) {}


