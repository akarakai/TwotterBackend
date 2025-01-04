package com.akaci.twotterbackend.application.dto.request;

import java.util.UUID;

public record CommentRequest(
        UUID twootId,
        String content
) {
}
