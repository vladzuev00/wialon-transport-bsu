package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.controller.tracker.view.TrackerView;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.TrackerService;
import org.junit.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class UniqueTrackerPhoneNumberValidatorTest {
    private final UniqueTrackerPhoneNumberValidator validator = new UniqueTrackerPhoneNumberValidator(null);

    @Test
    public void propertyShouldBeGot() {
        final String givenPhoneNumber = "447336934";
        final TrackerView givenView = createView(givenPhoneNumber);

        final String actual = validator.getProperty(givenView);
        assertSame(givenPhoneNumber, actual);
    }

    @Test
    public void trackerShouldBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "447336934";
        final TrackerService givenService = mock(TrackerService.class);

        final Tracker givenTracker = createTracker(255L);
        when(givenService.findByPhoneNumber(same(givenPhoneNumber))).thenReturn(Optional.of(givenTracker));

        final Optional<Tracker> optionalActual = validator.findByProperty(givenPhoneNumber, givenService);
        assertTrue(optionalActual.isPresent());
        final Tracker actual = optionalActual.get();
        assertSame(givenTracker, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "447336934";
        final TrackerService givenService = mock(TrackerService.class);

        when(givenService.findByPhoneNumber(same(givenPhoneNumber))).thenReturn(empty());

        final Optional<Tracker> optionalActual = validator.findByProperty(givenPhoneNumber, givenService);
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerView createView(final String phoneNumber) {
        final TrackerView givenView = mock(TrackerView.class);
        when(givenView.getPhoneNumber()).thenReturn(phoneNumber);
        return givenView;
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }
}
