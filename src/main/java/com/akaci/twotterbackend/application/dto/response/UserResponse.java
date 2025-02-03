package com.akaci.twotterbackend.application.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String username;
    private boolean isFollowed;

}
