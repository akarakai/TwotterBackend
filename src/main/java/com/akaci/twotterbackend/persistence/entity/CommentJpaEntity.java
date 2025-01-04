package com.akaci.twotterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "comment")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 300)
    private String content;

    @Column(name = "posted_at", nullable = false, updatable = false)
    private LocalDateTime postedAt;

    @ManyToOne
    @JoinColumn(name = "author_user_id")
    private UserJpaEntity author;

    @ManyToOne
    @JoinColumn(name = "twoot_id")
    private TwootJpaEntity twoot;

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "author_user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    @Builder.Default
    private Set<UserJpaEntity> likedByUsers = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommentJpaEntity that = (CommentJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
