package com.akaci.twotterbackend.application.dto.request;

public record LogInRequest(
        String username,
        String password
) {
}
