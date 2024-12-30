package com.akaci.twotterbackend.persistence.entity.joinEntity;

import com.akaci.twotterbackend.persistence.entity.CommentJpaEntity;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.entity.joinEntity.embeddedId.UserCommentLikeId;
import com.akaci.twotterbackend.persistence.entity.joinEntity.embeddedId.UserTwootLikeId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_like_comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserCommentLikeJpaEntity {

    @EmbeddedId
    private UserCommentLikeId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    private CommentJpaEntity comment;

    @Column(name = "liked_at", nullable = false)
    private LocalDateTime likedAt;
}
