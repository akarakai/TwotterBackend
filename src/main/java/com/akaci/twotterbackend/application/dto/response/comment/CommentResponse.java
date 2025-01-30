package com.akaci.twotterbackend.application.dto.response.comment;

import com.akaci.twotterbackend.application.dto.response.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID twootId,
        UUID commentId,
        UserResponse author,
        String content,
        int likes,
        LocalDateTime postedAt,
        boolean likedByUser
) {
}
