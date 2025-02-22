//package by.vladzuev.locationreceiver.trigger;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
//import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
//import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
//import by.vladzuev.locationreceiver.crud.entity.TrackerLastDataEntity;
//import org.junit.Test;
//import org.springframework.test.context.jdbc.Sql;
//
//import java.time.LocalDateTime;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//public final class DataTriggerTest extends AbstractSpringBootTest {
//
//    @Test
//    @Sql("classpath:sql/cities/insert-cities.sql")
//    public void trackerLastDataShouldBeUpdated() {
//        final Long givenTrackerId = 255L;
//        final LocationEntity givenData = LocationEntity.builder()
//                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 53))
//                .coordinate(new LocationEntity.Coordinate(53.233, 27.3434))
//                .speed(8)
//                .course(9)
//                .altitude(10)
//                .amountOfSatellites(11)
//                .hdop(12.4)
//                .inputs(13)
//                .outputs(14)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .tracker(entityManager.getReference(TrackerEntity.class, givenTrackerId))
//                .address(entityManager.getReference(AddressEntity.class, 255L))
//                .build();
//
//        final TrackerLastDataEntity beforeSavingData = findTrackerLastDataByTrackerId(givenTrackerId);
//        assertNull(beforeSavingData.getData());
//
//        entityManager.persist(givenData);
//        entityManager.flush();
//        entityManager.clear();
//
//        final TrackerLastDataEntity afterSavingData = findTrackerLastDataByTrackerId(givenTrackerId);
//        assertNotNull(afterSavingData.getData());
//    }
//
//    private TrackerLastDataEntity findTrackerLastDataByTrackerId(final Long trackerId) {
//        return entityManager.createQuery(
//                        "SELECT e FROM TrackerLastDataEntity e WHERE e.tracker.id = :trackerId",
//                        TrackerLastDataEntity.class
//                )
//                .setParameter("trackerId", trackerId)
//                .getSingleResult();
//    }
//}
