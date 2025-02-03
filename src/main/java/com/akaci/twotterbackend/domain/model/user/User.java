package com.akaci.twotterbackend.domain.model.user;

import com.akaci.twotterbackend.domain.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class User {

    private final UUID id;
    private final String username;
    private final Profile profile;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
