package ee.helmes.backend.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Optional;

@Component
@SessionScope
public class ProfileSession implements Serializable {

    private Long profileId;

    public Optional<Long> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}