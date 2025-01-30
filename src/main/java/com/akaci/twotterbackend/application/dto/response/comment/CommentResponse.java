package com.akaci.twotterbackend.application.dto.response.comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID twootId,
        UUID commentId,
        String author,
        String content,
        int likes,
        LocalDateTime postedAt,
        boolean likedByUser
) {
}
