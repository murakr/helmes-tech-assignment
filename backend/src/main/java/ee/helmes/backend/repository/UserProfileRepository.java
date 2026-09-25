package ee.helmes.backend.repository;

import ee.helmes.backend.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @EntityGraph(attributePaths = "sectors")
    Optional<UserProfile> findWithSectorsById(Long id);
}