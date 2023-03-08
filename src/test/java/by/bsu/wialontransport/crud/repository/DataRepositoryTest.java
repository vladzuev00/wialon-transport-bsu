package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Latitude;
import by.bsu.wialontransport.crud.entity.DataEntity.Longitude;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.base.AbstractContextTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public final class DataRepositoryTest extends AbstractContextTest {

    @Autowired
    private DataRepository repository;

    @Test
    @Sql(statements = "INSERT INTO data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    @Sql(statements = "INSERT INTO parameters(id, name, type, value, data_id) "
            + "VALUES(257, 'name', 'INTEGER', '44', 256)")
    public void dataShouldBeFoundById() {
        super.startQueryCount();
        final DataEntity actual = this.repository.findById(256L).orElseThrow();
        super.checkQueryCount(1);

        final DataEntity expected = DataEntity.builder()
                .id(256L)
                .date(LocalDate.of(2019, 10, 24))
                .time(LocalTime.of(14, 39, 53))
                .latitude(Latitude.builder()
                        .degrees(1)
                        .minutes(2)
                        .minuteShare(3)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(5)
                        .minutes(6)
                        .minuteShare(7)
                        .type(EAST)
                        .build())
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(List.of(super.entityManager.getReference(ParameterEntity.class, 257L)))
                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void dataShouldBeInserted() {
        final DataEntity givenData = DataEntity.builder()
                .date(LocalDate.of(2019, 10, 24))
                .time(LocalTime.of(14, 39, 53))
                .latitude(Latitude.builder()
                        .degrees(1)
                        .minutes(2)
                        .minuteShare(3)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(5)
                        .minutes(6)
                        .minuteShare(7)
                        .type(EAST)
                        .build())
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
                .build();
        final List<ParameterEntity> givenParameters = List.of(ParameterEntity.builder()
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(givenData)
                .build()
        );
        givenData.setParameters(givenParameters);

        super.startQueryCount();
        this.repository.save(givenData);
        super.checkQueryCount(4);
    }

    @Test
    @Sql(statements = "INSERT INTO data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(255, '2019-10-24', '14:39:52', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    @Sql(statements = "INSERT INTO data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    public void trackerLastDataShouldBeFoundByTrackerId() {
        super.startQueryCount();
        final DataEntity actual = this.repository.findTrackerLastDataByTrackerId(255L).orElseThrow();
        super.checkQueryCount(1);

        final DataEntity expected = DataEntity.builder()
                .id(256L)
                .date(LocalDate.of(2019, 10, 24))
                .time(LocalTime.of(14, 39, 53))
                .latitude(Latitude.builder()
                        .degrees(1)
                        .minutes(2)
                        .minuteShare(3)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(5)
                        .minutes(6)
                        .minuteShare(7)
                        .type(EAST)
                        .build())
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(emptyList())
                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundByTrackerId() {
        super.startQueryCount();
        final Optional<DataEntity> actual = this.repository.findTrackerLastDataByTrackerId(255L);
        super.checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }

    private static void checkEquals(final DataEntity expected, final DataEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.getLatitude(), actual.getLatitude());
        assertEquals(expected.getLongitude(), actual.getLongitude());
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getParameters(), actual.getParameters());
        assertEquals(expected.getTracker(), actual.getTracker());
    }
}
