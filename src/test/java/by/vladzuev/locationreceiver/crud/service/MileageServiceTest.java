package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.Mileage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class MileageServiceTest extends AbstractSpringBootTest {

    @Autowired
    private TrackerMileageService service;

    @Test
    public void mileageShouldBeIncreased() {
        final Tracker givenTracker = createTracker(255L);
        final double givenUrban = 5.5;
        final double givenCountry = 6.6;
        final by.vladzuev.locationreceiver.model.Mileage givenMileageDelta = new by.vladzuev.locationreceiver.model.Mileage(givenUrban, givenCountry);

        final int actualCountUpdatedRows = service.increaseMileage(givenTracker, givenMileageDelta);
        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final Optional<Mileage> optionalActual = service.findById(1L);
        assertTrue(optionalActual.isPresent());

        final Mileage actual = optionalActual.get();
        final Mileage expected = Mileage.builder()
                .id(1L)
                .urban(givenUrban)
                .country(givenCountry)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeIncreasedBecauseOfNotExistingTrackerId() {
        final Tracker givenTracker = createTracker(MAX_VALUE);
        final double givenUrban = 5.5;
        final double givenCountry = 6.6;
        final by.vladzuev.locationreceiver.model.Mileage givenMileageDelta = new by.vladzuev.locationreceiver.model.Mileage(givenUrban, givenCountry);

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
