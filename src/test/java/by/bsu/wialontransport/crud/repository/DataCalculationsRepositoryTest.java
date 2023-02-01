package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DataCalculationsRepositoryTest extends AbstractContextTest {

    @Autowired
    private DataCalculationsRepository repository;

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    @Sql(statements = "INSERT INTO tracker_last_data_calculations"
            + "(id, gps_odometer, ignition_on, engine_on_duration_seconds, data_id) "
            + "VALUES(257, 0.1, TRUE, 100, 256)")
    public void dataCalculationsShouldBeFoundById() {
        super.startQueryCount();
        final DataCalculationsEntity actual = this.repository.findById(257L).orElseThrow();
        super.checkQueryCount(1);

        final DataCalculationsEntity expected = DataCalculationsEntity.builder()
                .id(257L)
                .gpsOdometer(0.1)
                .ignitionOn(true)
                .engineOnDurationSeconds(100)
                .data(super.entityManager.getReference(DataEntity.class, 256L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    public void dataCalculationsShouldBeInserted() {
        final DataCalculationsEntity givenDataCalculations = DataCalculationsEntity.builder()
                .gpsOdometer(0.1)
                .ignitionOn(true)
                .engineOnDurationSeconds(100)
                .data(super.entityManager.getReference(DataEntity.class, 256L))
                .build();

        super.startQueryCount();
        this.repository.save(givenDataCalculations);
        super.checkQueryCount(1);
    }

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    @Sql(statements = "INSERT INTO tracker_last_data_calculations"
            + "(id, gps_odometer, ignition_on, engine_on_duration_seconds, data_id) "
            + "VALUES(257, 0.1, TRUE, 100, 256)")
    public void trackerLastDataCalculationsShouldBeFoundByTrackerId() {
        super.startQueryCount();
        final DataCalculationsEntity actual = this.repository.findTrackerLastDataCalculationsByTrackerId(255L)
                .orElseThrow();
        super.checkQueryCount(1);

        final DataCalculationsEntity expected = DataCalculationsEntity.builder()
                .id(257L)
                .gpsOdometer(0.1)
                .ignitionOn(true)
                .engineOnDurationSeconds(100)
                .data(super.entityManager.getReference(DataEntity.class, 256L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerLastDataCalculationsShouldNotBeFoundByTrackerId() {
        super.startQueryCount();
        final Optional<DataCalculationsEntity> optionalActual = this.repository
                .findTrackerLastDataCalculationsByTrackerId(255L);
        super.checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }

    private static void checkEquals(final DataCalculationsEntity expected, final DataCalculationsEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
        assertEquals(expected.isIgnitionOn(), actual.isIgnitionOn());
        assertEquals(expected.getEngineOnDurationSeconds(), actual.getEngineOnDurationSeconds());
        assertEquals(expected.getData(), actual.getData());
    }
}
