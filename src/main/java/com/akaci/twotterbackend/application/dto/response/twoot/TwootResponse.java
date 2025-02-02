package com.akaci.twotterbackend.application.dto.response.twoot;

import com.akaci.twotterbackend.application.dto.mapper.TwootMetadata;
import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.time.LocalDateTime;
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


