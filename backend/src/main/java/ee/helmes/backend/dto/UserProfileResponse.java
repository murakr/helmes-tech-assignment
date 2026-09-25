package ee.helmes.backend.dto;

import java.util.Set;

public record UserProfileResponse(Long id, String name, Set<Integer> sectorIds, boolean agreedToTerms) {
}
