package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.model.Mileage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerMileageServiceTest extends AbstractContextTest {

    @Autowired
    private TrackerMileageService service;

    @Test
    public void mileageShouldBeIncreased() {
        final Tracker givenTracker = createTracker(255L);
        final Mileage givenMileageDelta = new Mileage(5.5, 6.6);

        final int actualCountUpdatedRows = service.increaseMileage(givenTracker, givenMileageDelta);
        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final Optional<TrackerMileage> optionalActual = service.findById(1L);
        assertTrue(optionalActual.isPresent());
        final TrackerMileage actual = optionalActual.get();
        final TrackerMileage expected = TrackerMileage.builder()
                .id(1L)
                .urban(5.5)
                .country(6.6)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeIncreasedBecauseOfNotExistingTrackerId() {
        final Tracker givenTracker = createTracker(MAX_VALUE);
        final Mileage givenMileageDelta = new Mileage(5.5, 6.6);

        final int actualCountUpdatedRows = service.increaseMileage(givenTracker, givenMileageDelta);
        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }
}
