package com.akaci.twotterbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class Twoot implements Likable {

    private static final Logger LOGGER = LogManager.getLogger(Twoot.class);
    private static final int MAX_CONTENT_LENGTH = 300;

    private final UUID id;
    private final User author;
    private final String content;
    private final LocalDateTime postedAt;

    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    @Builder.Default
    private Set<User> likedByUsers = new HashSet<>();

    public Twoot(User author, String content) {
        this.id = UUID.randomUUID();
        validateContent(content);
        this.author = author;
        this.content = content;
        this.comments = new HashSet<>();
        this.likedByUsers = new HashSet<>();
        this.postedAt = LocalDateTime.now();
    }

    public Twoot(UUID id, User author, String content) {
        this.id = id;
        this.author = author;
        validateContent(content);
        this.content = content;
        this.comments = new HashSet<>();
        this.likedByUsers = new HashSet<>();
        this.postedAt = LocalDateTime.now();
    }


    public void addUserWhoLikesTwoot(User user) {
        if (likedByUsers.contains(user)) {
            throw new IllegalArgumentException("user already liked twoot");
        }
        likedByUsers.add(user);
    }

    // THIS NOT NEEDED? FOR NOW. Action is on the hand of the user
    public void removeUserWhoLikesTwoot(User user) {
        if (!likedByUsers.contains(user)) {
            throw new IllegalArgumentException("user not liked twoot");
        }
        likedByUsers.remove(user);
    }


    // MAYBE U DO NOT NEED THIS?
    public void addComment(Comment comment) {
        if (comments.contains(comment)) {
            throw new IllegalArgumentException("Comment already exists");
        }
        comments.add(comment);
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank() || content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Twoot Content is too long");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Twoot twoot)) return false;
        return Objects.equals(id, twoot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
