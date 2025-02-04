package com.akaci.twotterbackend.application.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CommentMetadata {

    private int likes;
    private LocalDateTime postedAt;
    private boolean likedByUser;

    public CommentMetadata() {
        this.likes = 0;
        this.postedAt = LocalDateTime.now();
        this.likedByUser = false;
    }

}
