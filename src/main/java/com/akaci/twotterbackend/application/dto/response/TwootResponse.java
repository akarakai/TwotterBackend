package com.akaci.twotterbackend.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TwootResponse(
        UUID id,
        String author,
        String content,
        int likes,
        LocalDateTime postedAt
) {
}
