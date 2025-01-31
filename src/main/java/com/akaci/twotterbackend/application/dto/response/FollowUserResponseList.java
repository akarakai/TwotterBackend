package com.akaci.twotterbackend.application.dto.response;

import java.util.List;

public record FollowUserResponseList(
        List<FollowUserResponse> followed
) {
}
