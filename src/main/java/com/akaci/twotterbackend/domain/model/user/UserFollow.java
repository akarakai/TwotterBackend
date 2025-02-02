package com.akaci.twotterbackend.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class UserFollow {

    private final UUID id;
    private final Set<User> followed = new HashSet<>();
    private final Set<User> followers = new HashSet<>();

    public void follow(User userToFollow) {

    }

    public void unfollow(User userToUnfollow) {

    }




}
