package com.akaci.twotterbackend.persistence.entity.joinEntity.embeddedId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTwootLikeId implements Serializable {

    private UUID userId;
    private UUID twootId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTwootLikeId that = (UserTwootLikeId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(twootId, that.twootId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, twootId);
    }

}
