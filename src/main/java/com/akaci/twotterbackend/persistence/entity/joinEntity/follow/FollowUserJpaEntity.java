package com.akaci.twotterbackend.persistence.entity.joinEntity.follow;

import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.entity.joinEntity.embeddedId.FollowSystemId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "followed_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FollowUserJpaEntity {

    @EmbeddedId
    private FollowSystemId id;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private UserJpaEntity follower;

    @ManyToOne
    @MapsId("followedId")
    @JoinColumn(name = "followed_id")
    private UserJpaEntity followed;

    @Column(name = "followed_at", nullable = false)
    private LocalDateTime followedAt;

}
