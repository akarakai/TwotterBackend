package com.akaci.twotterbackend.application.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        int code,
        String message,
        LocalDateTime timestamp
) {
}
