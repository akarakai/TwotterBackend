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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_name")
    private ProfileEntity profile;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "follower",
            joinColumns = @JoinColumn(name = "follower_user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id")
    )
    @Builder.Default
    private Set<UserEntity> followed = new HashSet<>();

    @ManyToMany(mappedBy = "followed")
    @Builder.Default
    private Set<UserEntity> followers = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private Set<TwootEntity> twoots = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private Set<CommentEntity> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "twoot_likes",
            joinColumns = @JoinColumn(name = "twoot_id"),
            inverseJoinColumns = @JoinColumn(name = "author_user_id")
    )
    @Builder.Default
    private Set<TwootEntity> likedTwoots = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "author_user_id")
    )
    @Builder.Default
    private Set<CommentEntity> likedComments = new HashSet<>();




    public UserEntity(UUID id, String username, ProfileEntity profile) {
        this.id = id;
        this.username = username;
        this.profile = profile;
    }

    public UserEntity(String username, ProfileEntity profile) {
        this.username = username;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity userJpa = (UserEntity) o;
        return Objects.equals(id, userJpa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
