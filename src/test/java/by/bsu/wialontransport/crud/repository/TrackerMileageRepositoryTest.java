package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil.checkEquals;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerMileageRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerMileageRepository repository;

    @Test
    @Sql("classpath:sql/tracker-mileage/insert-tracker-mileages.sql")
    public void mileageShouldBeFoundById() {
        final Long givenId = 255L;

        startQueryCount();
        final Optional<TrackerMileageEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerMileageEntity actual = optionalActual.get();
        final TrackerMileageEntity expected = TrackerMileageEntity.builder()
                .id(255L)
                .urban(100.1)
                .country(200.2)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeInserted() {
        final TrackerMileageEntity givenMileage = TrackerMileageEntity.builder()
                .urban(100.1)
                .country(200.2)
                .build();

        startQueryCount();
        repository.save(givenMileage);
        checkQueryCount(1);
    }

    @Test
    public void mileageShouldBeIncreased() {
        final Long givenTrackerId = 255L;
        final double givenUrbanDelta = 50.5;
        final double givenCountryDelta = 60.6;

        startQueryCount();
        final int actualCountUpdatedRows = repository.increaseMileage(
                givenTrackerId,
                givenUrbanDelta,
                givenCountryDelta
        );
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final TrackerMileageEntity actual = repository.findById(1L).orElseThrow();
        final TrackerMileageEntity expected = TrackerMileageEntity.builder()
                .id(1L)
                .urban(givenUrbanDelta)
                .country(givenCountryDelta)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void mileageShouldNotBeIncreasedBecauseOfNotExistingTrackerId() {
        final Long givenTrackerId = MAX_VALUE;
        final double givenUrbanDelta = 50.5;
        final double givenCountryDelta = 60.6;

        startQueryCount();
        final int actualCountUpdatedRows = repository.increaseMileage(
                givenTrackerId,
                givenUrbanDelta,
                givenCountryDelta
        );
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }
}
