package by.bsu.wialontransport.trigger;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerLastDataEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.entity.TrackerLastDataEntityUtil.checkEquals;
import static by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil.checkEquals;

public final class TrackerTriggerTest extends AbstractContextTest {

    @Test
    public void initialTrackerLastDataShouldBeInserted() {
        final List<TrackerLastDataEntity> actual = findAllTrackerLastDataOrderedById();
        final List<TrackerLastDataEntity> expected = List.of(
                createInitialTrackerLastDataEntity(1L, 255L),
                createInitialTrackerLastDataEntity(2L, 256L)
        );
        checkEquals(expected, actual);
    }

    @Test
    public void zeroMileagesShouldBeInserted() {
        final List<TrackerMileageEntity> actual = findAllMileagesOrderedById();
        final List<TrackerMileageEntity> expected = List.of(
                createZeroMileage(1L),
                createZeroMileage(2L)
        );
        checkEquals(expected, actual);
    }

    private List<TrackerLastDataEntity> findAllTrackerLastDataOrderedById() {
        return findEntities("SELECT e FROM TrackerLastDataEntity e ORDER BY e.id", TrackerLastDataEntity.class);
    }

    private <E extends Entity<?>> List<E> findEntities(final String query, final Class<E> entityType) {
        try (final Stream<E> stream = entityManager.createQuery(query, entityType).getResultStream()) {
            return stream.toList();
        }
    }

    private static TrackerLastDataEntity createInitialTrackerLastDataEntity(final Long id, final Long trackerId) {
        return TrackerLastDataEntity.builder()
                .id(id)
                .tracker(createTracker(trackerId))
                .build();
    }

    private static TrackerEntity createTracker(final Long id) {
        return TrackerEntity.builder()
                .id(id)
                .build();
    }

    private List<TrackerMileageEntity> findAllMileagesOrderedById() {
        return findEntities("SELECT e FROM TrackerMileageEntity e ORDER BY e.id", TrackerMileageEntity.class);
    }

    private static TrackerMileageEntity createZeroMileage(final Long id) {
        return TrackerMileageEntity.builder()
                .id(id)
                .urban(0)
                .country(0)
                .build();
    }
}
