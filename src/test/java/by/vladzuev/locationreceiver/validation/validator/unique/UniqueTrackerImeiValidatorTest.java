package by.vladzuev.locationreceiver.validation.validator.unique;

import by.vladzuev.locationreceiver.controller.tracker.view.TrackerView;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import org.junit.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class UniqueTrackerImeiValidatorTest {
    private final UniqueTrackerImeiValidator validator = new UniqueTrackerImeiValidator(null);

    @Test
    public void propertyShouldBeGot() {
        final String givenImei = "11112222333344445555";
        final TrackerView givenView = createView(givenImei);

        final String actual = validator.getProperty(givenView);
        assertSame(givenImei, actual);
    }

    @Test
    public void trackerShouldBeFoundByImei() {
        final String givenImei = "11112222333344445555";
        final TrackerService givenService = mock(TrackerService.class);

        final Tracker givenTracker = createTracker(255L);
        when(givenService.findByImei(same(givenImei))).thenReturn(Optional.of(givenTracker));

        final Optional<Tracker> optionalActual = validator.findByProperty(givenImei, givenService);
        assertTrue(optionalActual.isPresent());
        final Tracker actual = optionalActual.get();
        assertSame(givenTracker, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        final String givenImei = "11112222333344445555";
        final TrackerService givenService = mock(TrackerService.class);

        when(givenService.findByImei(same(givenImei))).thenReturn(empty());

        final Optional<Tracker> optionalActual = validator.findByProperty(givenImei, givenService);
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerView createView(final String imei) {
        final TrackerView givenView = mock(TrackerView.class);
        when(givenView.getImei()).thenReturn(imei);
        return givenView;
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }
}
