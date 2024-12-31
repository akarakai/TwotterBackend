package com.akaci.twotterbackend.persistence.entity;

import com.akaci.twotterbackend.persistence.entity.joinEntity.UserCommentLikeJpaEntity;
import com.akaci.twotterbackend.persistence.entity.joinEntity.UserTwootLikeJpaEntity;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;

import java.util.List;
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

    @OneToOne
    @JoinColumn(name = "account_id")
    private AccountJpaEntity account;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_name")
    private ProfileJpaEntity profile;

    @OneToMany(mappedBy = "author")
    private List<TwootJpaEntity> twoots;

    @OneToMany(mappedBy = "author")
    private List<CommentJpaEntity> comments;

    @OneToMany(mappedBy = "user")
    private List<UserTwootLikeJpaEntity> likedTwoots;

    @OneToMany(mappedBy = "user")
    private List<UserCommentLikeJpaEntity> likedComments;

    public UserJpaEntity(UUID id, String username, AccountJpaEntity account, ProfileJpaEntity profile) {
        this.id = id;
        this.username = username;
        this.account = account;
        this.profile = profile;
    }

    public UserJpaEntity(String username, AccountJpaEntity account, ProfileJpaEntity profile) {
        this.username = username;
        this.account = account;
        this.profile = profile;
    }


}
