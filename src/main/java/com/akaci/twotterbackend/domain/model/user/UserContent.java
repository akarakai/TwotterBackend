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
public class UserContent {

    private final UUID id;
    private final Set<Twoot> postedTwoots = new HashSet<>();
    private final Set<Comment> postedComments = new HashSet<>();

    public void addTwoot(Twoot twoot) {

    }

    public void addComment(Comment comment) {

    }

    public void removeTwoot(Twoot twoot) {

    }

    public void removeComment(Comment comment) {

    }

}
