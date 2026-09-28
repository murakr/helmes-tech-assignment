package ee.helmes.backend.exception;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(Long profileId) {
        super("Profile id not found: " + profileId);
    }
}
