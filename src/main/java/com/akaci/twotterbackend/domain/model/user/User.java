package com.akaci.twotterbackend.domain.model.user;

import com.akaci.twotterbackend.domain.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class User {

    private final UUID id;
    private final String username;
    private final Profile profile;

}
