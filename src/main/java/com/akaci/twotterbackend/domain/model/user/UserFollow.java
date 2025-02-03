package com.akaci.twotterbackend.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserFollow {

    private final UUID id;
    private final Set<User> followed = new HashSet<>();
    private final Set<User> followers = new HashSet<>();

    public void follow(User userToFollow) {
        if (followed.contains(userToFollow)) {
            throw new IllegalArgumentException("User is already followed");
        }

        followed.add(userToFollow);
    }

    public void unfollow(User userToUnfollow) {
        if (!followed.contains(userToUnfollow)) {
            throw new IllegalArgumentException("User is not followed");
        }

        followed.remove(userToUnfollow);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserFollow that = (UserFollow) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
