package ee.helmes.backend.service;

import ee.helmes.backend.entity.Sector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectorHierarchyTest {

    @Test
    void topLevelSectorHasLevelZero() {
        Sector manufacturing = sector(1, null);
        SectorHierarchy hierarchy = new SectorHierarchy(List.of(manufacturing));

        assertThat(hierarchy.levelOf(manufacturing)).isZero();
    }

    @Test
    void eachNestingStepAddsOneLevel() {
        Sector manufacturing = sector(1, null);
        Sector metalworking = sector(11, 1);
        Sector metalWorks = sector(542, 11);
        Sector cncMachining = sector(75, 542);
        SectorHierarchy hierarchy = new SectorHierarchy(
                List.of(manufacturing, metalworking, metalWorks, cncMachining));

        assertThat(hierarchy.levelOf(metalworking)).isEqualTo(1);
        assertThat(hierarchy.levelOf(metalWorks)).isEqualTo(2);
        assertThat(hierarchy.levelOf(cncMachining)).isEqualTo(3);
    }

    @Test
    void levelDoesNotDependOnListOrder() {
        Sector parent = sector(1, null);
        Sector child = sector(19, 1);
        Sector grandchild = sector(342, 19);
        SectorHierarchy hierarchy = new SectorHierarchy(List.of(grandchild, child, parent));

        assertThat(hierarchy.levelOf(grandchild)).isEqualTo(2);
    }

    private static Sector sector(int id, Integer parentId) {
        return new Sector(id, "Sector " + id, parentId, id);
    }
}