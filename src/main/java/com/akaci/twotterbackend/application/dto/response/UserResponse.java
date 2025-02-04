package com.akaci.twotterbackend.application.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String username;

    @JsonProperty("isFollowed")  // This tells Jackson to keep the "is" prefix
    @JsonAlias({"followed"})  // This tells Jackson to also accept "followed"
    private boolean isFollowed;

}
