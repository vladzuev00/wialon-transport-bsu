package by.bsu.wialontransport.trigger;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerLastDataEntity;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.util.entity.TrackerLastDataEntityUtil.checkEquals;

public final class DataTriggerTest extends AbstractContextTest {

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void trackerLastDataShouldBeUpdated() {
        final Long givenTrackerId = 255L;
        final DataEntity givenData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 53))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .tracker(entityManager.getReference(TrackerEntity.class, givenTrackerId))
                .address(entityManager.getReference(AddressEntity.class, 255L))
                .build();

        entityManager.persist(givenData);
        entityManager.flush();
        entityManager.clear();

        final TrackerLastDataEntity actual = findTrackerLastDataByTrackerId(givenTrackerId);
        final TrackerLastDataEntity expected = TrackerLastDataEntity.builder()
                .id(1L)
                .tracker(entityManager.getReference(TrackerEntity.class, givenTrackerId))
                .data(entityManager.getReference(DataEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    private TrackerLastDataEntity findTrackerLastDataByTrackerId(final Long trackerId) {
        return entityManager.createQuery(
                        "SELECT e FROM TrackerLastDataEntity e WHERE e.tracker.id = :trackerId",
                        TrackerLastDataEntity.class
                )
                .setParameter("trackerId", trackerId)
                .getSingleResult();
    }
}
