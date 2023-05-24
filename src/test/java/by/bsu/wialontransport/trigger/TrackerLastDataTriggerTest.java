package by.bsu.wialontransport.trigger;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerLastDataEntity;

import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

public final class TrackerLastDataTriggerTest extends AbstractContextTest {
    private static final String HQL_QUERY_TO_FIND_ALL_TRACKER_LAST_DATA = "SELECT e FROM TrackerLastDataEntity e";

    private static final String HQL_QUERY_TO_FIND_TRACKER_LAST_DATA_BY_TRACKER_ID
            = "SELECT e FROM TrackerLastDataEntity e WHERE e.tracker.id = :trackerId";
    private static final String NAME_NAMED_PARAMETER_TRACKER_ID = "trackerId";

    @Test
    public void trackerLastDataShouldBeInserted() {
        final Set<TrackerLastDataEntity> actual = this.findAllTrackerLastData();
        final Set<Long> actualTrackerIds = findTrackerIds(actual);
        final Set<Long> expectedTrackerIds = Set.of(255L);
        assertEquals(expectedTrackerIds, actualTrackerIds);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(254, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id, address_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 254)")
    public void trackerLastDataShouldBeUpdatedAfterInsertingMessage() {
        final TrackerLastDataEntity actual = this.findTrackerLastDataByTrackerId(255L);
        assertEquals(256L, actual.getData().getId().longValue());
    }

    private Set<TrackerLastDataEntity> findAllTrackerLastData() {
        return super.entityManager
                .createQuery(HQL_QUERY_TO_FIND_ALL_TRACKER_LAST_DATA, TrackerLastDataEntity.class)
                .getResultStream()
                .collect(toSet());
    }

    private static Set<Long> findTrackerIds(final Set<TrackerLastDataEntity> trackerLastData) {
        return trackerLastData.stream()
                .map(TrackerLastDataEntity::getTracker)
                .map(TrackerEntity::getId)
                .collect(toSet());
    }

    @SuppressWarnings("all")
    private TrackerLastDataEntity findTrackerLastDataByTrackerId(Long trackerId) {
        return (TrackerLastDataEntity) super.entityManager
                .createQuery(HQL_QUERY_TO_FIND_TRACKER_LAST_DATA_BY_TRACKER_ID)
                .setParameter(NAME_NAMED_PARAMETER_TRACKER_ID, trackerId)
                .getSingleResult();
    }
}
