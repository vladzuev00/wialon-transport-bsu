package by.bsu.wialontransport.trigger;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerOdometerEntity;
import org.junit.Test;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

public final class TrackerOdometerTriggerTest extends AbstractContextTest {
    private static final String HQL_QUERY_TO_FIND_ALL_TRACKER_ODOMETERS = "SELECT e FROM TrackerOdometerEntity e";

    @Test
    public void trackerOdometersShouldBeInserted() {
        final Set<TrackerOdometerEntity> actual = this.findAllTrackerOdometers();
        assertEquals(1, actual.size());
        final TrackerOdometerEntity foundTrackerOdometer = actual.iterator().next();
        assertEquals(0, foundTrackerOdometer.getUrban(), 0);
        assertEquals(0, foundTrackerOdometer.getCountry(), 0);
    }

    private Set<TrackerOdometerEntity> findAllTrackerOdometers() {
        return super.entityManager
                .createQuery(HQL_QUERY_TO_FIND_ALL_TRACKER_ODOMETERS, TrackerOdometerEntity.class)
                .getResultStream()
                .collect(toSet());
    }
}
