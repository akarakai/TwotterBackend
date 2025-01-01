package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.domain.Profile;
import com.akaci.twotterbackend.domain.User;

public interface ProfileCrudService {

    Profile getProfileFromUsername(String username);
    Profile updateProfileDescription(String username, String newDescription);
}
