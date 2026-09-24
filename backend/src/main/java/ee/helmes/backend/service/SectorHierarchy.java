package ee.helmes.backend.service;

import ee.helmes.backend.entity.Sector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class SectorHierarchy {

    private final Map<Integer, Sector> sectorsById;
    private final Map<Integer, Integer> levelsById = new HashMap<>();

    SectorHierarchy(List<Sector> sectors) {
        this.sectorsById = sectors.stream()
                .collect(Collectors.toMap(Sector::getId, Function.identity()));
    }

    int levelOf(Sector sector) {
        Integer knownLevel = levelsById.get(sector.getId());
        if (knownLevel != null) {
            return knownLevel;
        }
        Integer parentId = sector.getParentId();
        int level = parentId == null ? 0 : levelOf(sectorsById.get(parentId)) + 1;
        levelsById.put(sector.getId(), level);
        return level;
    }
}