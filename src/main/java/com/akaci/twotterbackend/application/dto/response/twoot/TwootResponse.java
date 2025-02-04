package com.akaci.twotterbackend.application.dto.response.twoot;

import com.akaci.twotterbackend.application.dto.response.UserResponse;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TwootResponse {

    private UUID id;
    private String content;
    private UserResponse author;
    private TwootMetadata metadata;

}


