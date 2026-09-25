package ee.helmes.backend.dto;

import jakarta.validation.constraints.*;

import java.util.Set;

public record UserProfileRequest(
        @NotBlank(message = "Name is required!")
        @Size(max = 100, message = "Name cannot exceed 100 characters!")
        String name,

        @NotEmpty(message = "Please select at least one sector!")
        Set<@NotNull Integer> sectorIds,

        @AssertTrue(message = "You must accept the terms!")
        boolean agreedToTerms
) {
}
