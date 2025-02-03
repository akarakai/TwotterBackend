package com.akaci.twotterbackend.domain.model.user;

import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.domain.model.Twoot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserLike {

    private final UUID id;

    private final Set<Twoot> likedTwoots = new HashSet<>();

    private final Set<Comment> likedComments = new HashSet<>();

    public void likeTwoot(Twoot twoot) {

    }

    public void likeComment(Comment comment) {

    }

    public void unLikeTwoot(Twoot twoot) {

    }

    public void unLikeComment(Comment comment) {

    }
}
