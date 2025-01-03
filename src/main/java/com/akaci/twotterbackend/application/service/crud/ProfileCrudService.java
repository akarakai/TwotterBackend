package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.domain.model.Profile;

public interface ProfileCrudService {

    Profile getProfileFromUsername(String username);
    Profile updateProfileDescription(String username, String newDescription);
}
