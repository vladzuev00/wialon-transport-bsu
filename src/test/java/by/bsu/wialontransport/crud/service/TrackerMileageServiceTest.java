package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.model.Mileage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerMileageServiceTest extends AbstractContextTest {

    @Autowired
    private TrackerMileageService service;

    @Test
    public void mileageShouldBeIncreased() {
        final Tracker givenTracker = createTracker(255L);
        final Mileage givenMileageDelta = new Mileage(5.5, 6.6);

        this.service.increaseMileage(givenTracker, givenMileageDelta);

        final Optional<TrackerMileage> optionalActual = this.service.findById(1L);
        assertTrue(optionalActual.isPresent());
        final TrackerMileage actual = optionalActual.get();
        final TrackerMileage expected = TrackerMileage.builder()
                .id(1L)
                .urban(5.5)
                .country(6.6)
                .build();
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }
}
