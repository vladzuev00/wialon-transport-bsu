package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.DataEntity.Latitude;
import by.bsu.wialontransport.crud.entity.DataEntity.Longitude;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;

//public final class ExtendedDataRepositoryTest extends AbstractContextTest {
//
//    @Autowired
//    private ExtendedDataRepository repository;
//
//    @Test
//    @Sql(statements = "INSERT INTO tracker_last_data"
//            + "(id, date, time, "
//            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
//            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
//            + "speed, course, height, amount_of_satellites, tracker_id) "
//            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
//    @Sql(statements = "INSERT INTO tracker_last_extended_data"
//            + "(id, reduction_precision, inputs, outputs, analog_inputs, driver_key_code) "
//            + "VALUES(255, 0.1, 12, 13, ARRAY[0.2, 0.3, 0.4], 'driver key code')")
//    public void extendedDataShouldBeFoundById() {
//        super.startQueryCount();
//        final ExtendedDataEntity actual = this.repository.findById(255L).orElseThrow();
//        super.checkQueryCount(1);
//        final ExtendedDataEntity expected = ExtendedDataEntity.extendedDataEntityBuilder()
//                .id(255L)
//                .date(LocalDate.of(2019, 10, 24))
//                .time(LocalTime.of(14, 39, 53))
//                .latitude(Latitude.builder()
//                        .degrees(1)
//                        .minutes(2)
//                        .minuteShare(3)
//                        .type(NORTH)
//                        .build())
//                .longitude(Longitude.builder()
//                        .degrees(5)
//                        .minutes(6)
//                        .minuteShare(7)
//                        .type(EAST)
//                        .build())
//                .speed(8)
//                .course(9)
//                .height(10)
//                .amountOfSatellites(11)
//                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
//                .reductionPrecision(0.1)
//                .inputs(12)
//                .outputs(13)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    public void extendedDataShouldBeInserted() {
//        final ExtendedDataEntity givenExtendedData = ExtendedDataEntity.extendedDataEntityBuilder()
//                .date(LocalDate.of(2019, 10, 24))
//                .time(LocalTime.of(14, 39, 53))
//                .latitude(Latitude.builder()
//                        .degrees(1)
//                        .minutes(2)
//                        .minuteShare(3)
//                        .type(NORTH)
//                        .build())
//                .longitude(Longitude.builder()
//                        .degrees(5)
//                        .minutes(6)
//                        .minuteShare(7)
//                        .type(EAST)
//                        .build())
//                .speed(8)
//                .course(9)
//                .height(10)
//                .amountOfSatellites(11)
//                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
//                .reductionPrecision(0.1)
//                .inputs(12)
//                .outputs(13)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .build();
//
//        super.startQueryCount();
//        this.repository.save(givenExtendedData);
//        super.checkQueryCount(2);
//    }
//
//    private static void checkEquals(final ExtendedDataEntity expected, final ExtendedDataEntity actual) {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getDate(), actual.getDate());
//        assertEquals(expected.getTime(), actual.getTime());
//        assertEquals(expected.getLatitude(), actual.getLatitude());
//        assertEquals(expected.getLongitude(), actual.getLongitude());
//        assertEquals(expected.getSpeed(), actual.getSpeed());
//        assertEquals(expected.getCourse(), actual.getCourse());
//        assertEquals(expected.getHeight(), actual.getHeight());
//        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
//        assertEquals(expected.getTracker(), actual.getTracker());
//        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
//        assertEquals(expected.getInputs(), actual.getInputs());
//        assertEquals(expected.getOutputs(), actual.getOutputs());
//        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
//        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
//    }
//}
