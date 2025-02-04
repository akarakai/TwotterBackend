package com.akaci.twotterbackend.application.dto.response.twoot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class TwootMetadata {

    private int likes;
    private int commentNumber;
    private LocalDateTime postedAt;
    private boolean likedByUser;

}
