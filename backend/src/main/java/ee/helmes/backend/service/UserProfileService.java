package ee.helmes.backend.service;

import ee.helmes.backend.dto.UserProfileRequest;
import ee.helmes.backend.dto.UserProfileResponse;
import ee.helmes.backend.entity.Sector;
import ee.helmes.backend.entity.UserProfile;
import ee.helmes.backend.exception.ProfileNotFoundException;
import ee.helmes.backend.exception.UnknownSectorException;
import ee.helmes.backend.repository.SectorRepository;
import ee.helmes.backend.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final SectorRepository sectorRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, SectorRepository sectorRepository) {
        this.userProfileRepository = userProfileRepository;
        this.sectorRepository = sectorRepository;
    }

    @Transactional(readOnly = true)
    public Optional<UserProfileResponse> findById(Long profileId) {
        return userProfileRepository.findWithSectorsById(profileId).map(this::toResponse);
    }

    public UserProfileResponse create(UserProfileRequest request) {
        UserProfile profile = new UserProfile(
                request.name().strip(),
                findSectors(request.sectorIds()),
                request.agreedToTerms()
        );
        return toResponse(userProfileRepository.save(profile));
    }

    public UserProfileResponse update(Long profileId, UserProfileRequest request) {
        UserProfile profile = userProfileRepository.findWithSectorsById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));
        profile.update(request.name().strip(), findSectors(request.sectorIds()), request.agreedToTerms());
        return toResponse(profile);
    }

    private Set<Sector> findSectors(Set<Integer> sectorIds) {
        List<Sector> sectors = sectorRepository.findAllById(sectorIds);
        if (sectors.size() != sectorIds.size()) {
            Set<Integer> foundIds = sectors.stream().map(Sector::getId).collect(Collectors.toSet());
            Set<Integer> unknownIds = sectorIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());
            throw new UnknownSectorException(unknownIds);
        }
        return new HashSet<>(sectors);
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        Set<Integer> sectorIds = profile.getSectors().stream()
                .map(Sector::getId)
                .collect(Collectors.toSet());
        return new UserProfileResponse(profile.getId(), profile.getName(), sectorIds, profile.isAgreedToTerms());
    }
}