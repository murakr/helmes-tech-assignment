package ee.helmes.backend.service;

import ee.helmes.backend.dto.SectorResponse;
import ee.helmes.backend.entity.Sector;
import ee.helmes.backend.repository.SectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectorService {

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public List<SectorResponse> getSectors() {
        List<Sector> sectors = sectorRepository.findAllByOrderBySortOrderAsc();
        SectorHierarchy hierarchy = new SectorHierarchy(sectors);
        return sectors.stream()
                .map(sector -> new SectorResponse(sector.getId(), sector.getName(), hierarchy.levelOf(sector)))
                .toList();
    }
}