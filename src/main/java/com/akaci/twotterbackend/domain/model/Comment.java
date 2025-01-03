package com.akaci.twotterbackend.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class Comment {

    private final UUID id;
    private final String content;
    private final User author;
    @Builder.Default
    private final Set<User> likedByUsers = new HashSet<>();
    private Twoot twoot;
}
