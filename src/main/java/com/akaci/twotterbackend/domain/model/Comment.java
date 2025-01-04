package com.akaci.twotterbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class Comment {

    private final UUID id;
    private final String content;
    private final User author;
    @Builder.Default
    private final Set<User> likedByUsers = new HashSet<>();
    private Twoot twoot;
    private LocalDateTime postedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
