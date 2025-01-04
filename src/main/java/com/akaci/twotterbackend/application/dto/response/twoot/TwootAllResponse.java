package com.akaci.twotterbackend.application.dto.response.twoot;

import java.time.LocalDateTime;
import java.util.List;

public record TwootAllResponse(
        int totalTwootsNumber,
        LocalDateTime lastTwootPostedAt,
        List<TwootResponse> twoots
) {
}
