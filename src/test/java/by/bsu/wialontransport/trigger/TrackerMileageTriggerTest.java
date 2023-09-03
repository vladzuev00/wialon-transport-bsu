package by.bsu.wialontransport.trigger;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.junit.Test;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

public final class TrackerMileageTriggerTest extends AbstractContextTest {
    private static final String HQL_QUERY_TO_FIND_ALL_TRACKER_MILEAGES = "SELECT e FROM TrackerMileageEntity e";

    @Test
    public void trackerMileagesShouldBeInserted() {
        final Set<TrackerMileageEntity> actual = this.findAllTrackerMileages();
        assertEquals(1, actual.size());
        final TrackerMileageEntity foundTrackerMileage = actual.iterator().next();
        assertEquals(0, foundTrackerMileage.getUrban(), 0);
        assertEquals(0, foundTrackerMileage.getCountry(), 0);
    }

    private Set<TrackerMileageEntity> findAllTrackerMileages() {
        return super.entityManager
                .createQuery(HQL_QUERY_TO_FIND_ALL_TRACKER_MILEAGES, TrackerMileageEntity.class)
                .getResultStream()
                .collect(toSet());
    }
}
