package com.akaci.twotterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "`user`")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_name")
    private ProfileJpaEntity profile;

    @ManyToMany
    @JoinTable(
            name = "follower",
            joinColumns = @JoinColumn(name = "follower_user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id")
    )
    @Builder.Default
    private Set<UserJpaEntity> followed = new HashSet<>();

    @ManyToMany(mappedBy = "followed")
    @Builder.Default
    private Set<UserJpaEntity> followers = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private Set<TwootJpaEntity> twoots = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private Set<CommentJpaEntity> comments = new HashSet<>();

    @ManyToMany(mappedBy = "likedByUsers")
    @Builder.Default
    private Set<TwootJpaEntity> likedTwoots = new HashSet<>();

    @ManyToMany(mappedBy = "likedByUsers")
    @Builder.Default
    private Set<CommentJpaEntity> likedComments = new HashSet<>();

    public UserJpaEntity(UUID id, String username, ProfileJpaEntity profile) {
        this.id = id;
        this.username = username;
        this.profile = profile;
    }

    public UserJpaEntity(String username, ProfileJpaEntity profile) {
        this.username = username;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserJpaEntity userJpa = (UserJpaEntity) o;
        return Objects.equals(id, userJpa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
