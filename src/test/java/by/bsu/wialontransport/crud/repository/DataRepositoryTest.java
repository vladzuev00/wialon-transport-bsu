package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Latitude;
import by.bsu.wialontransport.crud.entity.DataEntity.Longitude;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.base.AbstractContextTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//public final class DataRepositoryTest extends AbstractContextTest {
//
//    @Autowired
//    private DataRepository repository;
//
//    @Test
//    @Sql(statements = "INSERT INTO tracker_last_data"
//            + "(id, date, time, "
//            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
//            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
//            + "speed, course, height, amount_of_satellites, tracker_id) "
//            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
//    public void dataShouldBeFoundById() {
//        super.startQueryCount();
//        final DataEntity actual = this.repository.findById(255L).orElseThrow();
//        super.checkQueryCount(1);
//
//        final DataEntity expected = DataEntity.dataEntityBuilder()
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
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    public void dataShouldBeInserted() {
//        final DataEntity givenData = DataEntity.dataEntityBuilder()
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
//                .build();
//        super.startQueryCount();
//        this.repository.save(givenData);
//        super.checkQueryCount(1);
//    }
//
//    @Test
//    @Sql(statements = "INSERT INTO tracker_last_data"
//            + "(id, date, time, "
//            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
//            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
//            + "speed, course, height, amount_of_satellites, tracker_id) "
//            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
//    public void trackerLastDataShouldBeFoundByTrackerId() {
//        super.startQueryCount();
//        final DataEntity actual = this.repository.findTrackerLastData(255L).orElseThrow();
//        super.checkQueryCount(1);
//
//        final DataEntity expected = DataEntity.dataEntityBuilder()
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
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    public void trackerLastDataShouldNotBeFoundByTrackerId() {
//        super.startQueryCount();
//        final Optional<DataEntity> optionalFoundData = this.repository.findTrackerLastData(255L);
//        super.checkQueryCount(1);
//        assertTrue(optionalFoundData.isEmpty());
//    }
//
//    private static void checkEquals(final DataEntity expected, final DataEntity actual) {
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
//    }
//}
