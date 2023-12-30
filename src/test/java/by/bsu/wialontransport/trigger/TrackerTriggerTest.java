package by.bsu.wialontransport.trigger;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil.checkEquals;

public final class TrackerTriggerTest extends AbstractContextTest {

    @Test
    public void zeroMileagesShouldBeInserted() {
        final List<TrackerMileageEntity> actual = findAllMileagesOrderedById();
        final List<TrackerMileageEntity> expected = List.of(
                createZeroMileage(1L),
                createZeroMileage(2L)
        );
        checkEquals(expected, actual);
    }

    private List<TrackerMileageEntity> findAllMileagesOrderedById() {
        return entityManager
                .createQuery("SELECT e FROM TrackerMileageEntity e ORDER BY e.id", TrackerMileageEntity.class)
                .getResultStream()
                .toList();
    }

    private static TrackerMileageEntity createZeroMileage(final Long id) {
        return TrackerMileageEntity.builder()
                .id(id)
                .urban(0)
                .country(0)
                .build();
    }
}
