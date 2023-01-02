package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;

public final class DataCalculationsRepositoryTest extends AbstractContextTest {

    @Autowired
    private DataCalculationsRepository repository;

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, height, amount_of_satellites, tracker_id) "
            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
    @Sql(statements = "INSERT INTO tracker_last_data_calculations"
            + "(id, gps_odometer, ignition_on, engine_on_duration_seconds, acceleration, data_id) "
            + "VALUES(255, 0.1, TRUE, 100, 5.5, 255)")
    public void dataCalculationsShouldBeFoundById() {
        super.startQueryCount();
        final DataCalculationsEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final DataCalculationsEntity expected = DataCalculationsEntity.builder()
                .id(255L)
                .gpsOdometer(0.1)
                .ignitionOn(true)
                .engineOnDurationSeconds(100)
                .acceleration(5.5)
                .data(super.entityManager.getReference(DataEntity.class, 255L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, height, amount_of_satellites, tracker_id) "
            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
    public void dataCalculationsShouldBeInserted() {
        final DataCalculationsEntity givenDataCalculations = DataCalculationsEntity.builder()
                .gpsOdometer(0.1)
                .ignitionOn(true)
                .engineOnDurationSeconds(100)
                .acceleration(5.5)
                .data(super.entityManager.getReference(DataEntity.class, 255L))
                .build();

        super.startQueryCount();
        this.repository.save(givenDataCalculations);
        super.checkQueryCount(1);
    }

    private static void checkEquals(final DataCalculationsEntity expected, final DataCalculationsEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
        assertEquals(expected.isIgnitionOn(), actual.isIgnitionOn());
        assertEquals(expected.getEngineOnDurationSeconds(), actual.getEngineOnDurationSeconds());
        assertEquals(expected.getData(), actual.getData());
    }
}
