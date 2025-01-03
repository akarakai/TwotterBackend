package com.akaci.twotterbackend.domain.model;

import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.exceptions.UserAlreadyFollowedException;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class User {

    private static final Logger LOGGER = LogManager.getLogger(User.class);

    private final UUID id;
    private String username;
    private Profile profile;

    @Builder.Default
    private Set<User> followed = new HashSet<>();
    @Builder.Default
    private Set<User> followers = new HashSet<>();

    // Constructor with all attributes
    public User(String username, Profile profile) {
        this.id = UUID.randomUUID();
        validateName(username);
        this.username = username;
        this.profile = profile;
    }

    public User(UUID id, String username, Profile profile) {
        this.id = id;
        validateName(username);
        this.username = username;
    }

    public void changeUsername(String username) {
        validateName(username);
        this.username = username;
    }

    // Constructor without the ID (for new users, for example)
//    public User(String username, Profile profile) {
//        validateName(username);
//        this.username = username;
//        this.profile = profile;
//    }

    // Constructor with just a username (could be used when minimal information is needed)
    public User(String username) {
        this.id = UUID.randomUUID();
        validateName(username);
        this.username = username;
    }

    // Add a user to the 'followed' set
    // TODO RECURSIVE ERROR?
    public void follow(User user) {
        if (followed.contains(user)) {
            throw new UserAlreadyFollowedException();
        }
        followed.add(user);
        user.addToFollowers(this);
    }

    private void addToFollowers(User user) {
        if (followers.contains(user)) {
            LOGGER.warn("User already followed");
            return;
        }
        followers.add(user);
    }

    // Override equals and hashCode for proper comparison based on the unique 'id'
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Username validation method
    private void validateName(String name) {
        UsernameValidator.validate(name); // Assuming this is a valid class handling username validation
    }
}