package ee.helmes.backend.service;

import ee.helmes.backend.dto.UserProfileRequest;
import ee.helmes.backend.dto.UserProfileResponse;
import ee.helmes.backend.entity.Sector;
import ee.helmes.backend.entity.UserProfile;
import ee.helmes.backend.exception.ProfileNotFoundException;
import ee.helmes.backend.exception.UnknownSectorException;
import ee.helmes.backend.repository.SectorRepository;
import ee.helmes.backend.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    private static final Sector MANUFACTURING = new Sector(1, "Manufacturing", null, 1);
    private static final Sector CONSTRUCTION_MATERIALS = new Sector(19, "Construction materials", 1, 2);

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private SectorRepository sectorRepository;

    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        userProfileService = new UserProfileService(userProfileRepository, sectorRepository);
    }

    @Test
    void createStripsNameAndLinksRequestedSectors() {
        when(sectorRepository.findAllById(Set.of(1, 19)))
                .thenReturn(List.of(MANUFACTURING, CONSTRUCTION_MATERIALS));
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfileResponse response = userProfileService.create(
                new UserProfileRequest("  testUser ", Set.of(1, 19), true));

        assertThat(response.name()).isEqualTo("testUser");
        assertThat(response.sectorIds()).containsExactlyInAnyOrder(1, 19);
        assertThat(response.agreedToTerms()).isTrue();
    }

    @Test
    void createRejectsUnknownSectorsWithoutSaving() {
        when(sectorRepository.findAllById(Set.of(1, 99999))).thenReturn(List.of(MANUFACTURING));

        assertThatThrownBy(() -> userProfileService.create(
                new UserProfileRequest("testUser", Set.of(1, 99999), true)))
                .isInstanceOf(UnknownSectorException.class)
                .hasMessageContaining("99999");

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void updateReplacesDataOfExistingProfileWithoutExplicitSave() {
        UserProfile existingProfile = new UserProfile("testUserOld", Set.of(MANUFACTURING), true);
        when(userProfileRepository.findWithSectorsById(1L)).thenReturn(Optional.of(existingProfile));
        when(sectorRepository.findAllById(Set.of(19))).thenReturn(List.of(CONSTRUCTION_MATERIALS));

        UserProfileResponse response = userProfileService.update(
                1L, new UserProfileRequest("testUser New", Set.of(19), true));

        assertThat(response.name()).isEqualTo("testUser New");
        assertThat(response.sectorIds()).containsExactly(19);
        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void updateFailsWhenProfileDoesNotExist() {
        when(userProfileRepository.findWithSectorsById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.update(
                1L, new UserProfileRequest("testUser", Set.of(1), true)))
                .isInstanceOf(ProfileNotFoundException.class);
    }

    @Test
    void findByIdReturnsEmptyWhenProfileDoesNotExist() {
        when(userProfileRepository.findWithSectorsById(1L)).thenReturn(Optional.empty());

        assertThat(userProfileService.findById(1L)).isEmpty();
    }
}