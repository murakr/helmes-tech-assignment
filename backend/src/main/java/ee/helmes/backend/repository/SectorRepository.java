package ee.helmes.backend.repository;

import ee.helmes.backend.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Integer> {

    List<Sector> findAllByOrderBySortOrderAsc();
}