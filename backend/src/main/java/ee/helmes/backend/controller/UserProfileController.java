package ee.helmes.backend.controller;

import ee.helmes.backend.dto.UserProfileRequest;
import ee.helmes.backend.dto.UserProfileResponse;
import ee.helmes.backend.service.UserProfileService;
import ee.helmes.backend.session.ProfileSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final ProfileSession profileSession;

    public UserProfileController(UserProfileService userProfileService, ProfileSession profileSession) {
        this.userProfileService = userProfileService;
        this.profileSession = profileSession;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile() {
        return profileSession.getProfileId()
                .flatMap(userProfileService::findById)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping
    public UserProfileResponse saveProfile(@Valid @RequestBody UserProfileRequest request) {
        UserProfileResponse savedProfile = profileSession.getProfileId()
                .map(profileId -> userProfileService.update(profileId, request))
                .orElseGet(() -> userProfileService.create(request));
        profileSession.setProfileId(savedProfile.id());
        return savedProfile;
    }
}