package by.vladzuev.locationreceiver.trigger;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.AbstractEntity;
import by.vladzuev.locationreceiver.crud.entity.LastLocationEntity;
import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import by.vladzuev.locationreceiver.util.entity.TrackerLastDataEntityUtil;
import by.vladzuev.locationreceiver.util.entity.MileageEntityUtil;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

public final class TrackerTriggerTest extends AbstractSpringBootTest {

    @Test
    public void initialTrackerLastDataShouldBeInserted() {
        final List<LastLocationEntity> actual = findAllTrackerLastDataOrderedById();
        final List<LastLocationEntity> expected = List.of(
                createInitialTrackerLastDataEntity(1L, 255L),
                createInitialTrackerLastDataEntity(2L, 256L)
        );
        TrackerLastDataEntityUtil.checkEquals(expected, actual);
    }

    @Test
    public void zeroMileagesShouldBeInserted() {
        final List<MileageEntity> actual = findAllMileagesOrderedById();
        final List<MileageEntity> expected = List.of(
                createZeroMileage(1L),
                createZeroMileage(2L)
        );
        MileageEntityUtil.assertEquals(expected, actual);
    }

    private List<LastLocationEntity> findAllTrackerLastDataOrderedById() {
        return findEntities("SELECT e FROM TrackerLastDataEntity e ORDER BY e.id", LastLocationEntity.class);
    }

    private <E extends AbstractEntity<?>> List<E> findEntities(final String query, final Class<E> entityType) {
        try (final Stream<E> stream = entityManager.createQuery(query, entityType).getResultStream()) {
            return stream.toList();
        }
    }

    private static LastLocationEntity createInitialTrackerLastDataEntity(final Long id, final Long trackerId) {
        return LastLocationEntity.builder()
                .id(id)
                .tracker(createTracker(trackerId))
                .build();
    }

    private static TrackerEntity createTracker(final Long id) {
        return TrackerEntity.builder()
                .id(id)
                .build();
    }

    private List<MileageEntity> findAllMileagesOrderedById() {
        return findEntities("SELECT e FROM TrackerMileageEntity e ORDER BY e.id", MileageEntity.class);
    }

    private static MileageEntity createZeroMileage(final Long id) {
        return MileageEntity.builder()
                .id(id)
                .urban(0)
                .country(0)
                .build();
    }
}
