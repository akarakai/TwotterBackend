package com.akaci.twotterbackend.application.NEWSERVICE;

import com.akaci.twotterbackend.application.dto.request.ProfileRequest;
import com.akaci.twotterbackend.application.dto.response.ProfileResponse;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.ProfileEntity;
import com.akaci.twotterbackend.persistence.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileService {

    private final ProfileRepository profileRepo;

    public ProfileService(ProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    public ProfileResponse getProfile(String username) {
        ProfileEntity profileEntity = profileRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return new ProfileResponse(profileEntity.getName(), profileEntity.getDescription());
    }

    public ProfileResponse updateDescription(String username, String newDescription) {
            validateDescription(newDescription);
            profileRepo.setDescription(username, newDescription);
            return new ProfileResponse(username, newDescription);
    }

    private void validateDescription(String description) {
        if (description.length() > 200) {
            throw new BadRequestExceptionResponse("Description too long");
        }
    }


}
