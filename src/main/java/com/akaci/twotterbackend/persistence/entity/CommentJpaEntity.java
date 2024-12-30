package com.akaci.twotterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @ManyToMany
    private Set<UserJpaEntity> likedByUsers;

}
