package com.akaci.twotterbackend.application.dto.response.comment;

import com.akaci.twotterbackend.application.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponse {

    private UUID commentId;
    private UUID twootId;
    private String content;
    private UserResponse author;
    private CommentMetadata metadata;
}
