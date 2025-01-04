package com.akaci.twotterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "twoot")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TwootJpaEntity {

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

    @OneToMany(mappedBy = "twoot")
    @Builder.Default
    private Set<CommentJpaEntity> comments = new HashSet<>();

    @ManyToMany(mappedBy = "likedTwoots")
    @Builder.Default
    private Set<UserJpaEntity> likedByUsers = new HashSet<>();



//    @ManyToMany
//    @JoinTable(
//            name = "twoot_likes",
//            joinColumns = @JoinColumn(name = "author_user_id"),
//            inverseJoinColumns = @JoinColumn(name = "twoot_id")
//    )
//    @Builder.Default
//    // the boss of the many to many relation.
//    // I just need only to update this and persist.
//    // PROBLEM
//    // This should not be the boss of the relation, because this set
//    // will be extremely large . So we have to use mappedBy
//    private Set<UserJpaEntity> likedByUsers = new HashSet<>();





}
