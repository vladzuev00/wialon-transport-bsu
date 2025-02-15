package by.vladzuev.locationreceiver.controller.tracker.view;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class ResponseTrackerViewTest {

    @Test
    public void viewShouldBeFoundById() {
        final Long givenId = 255L;
        final ResponseTrackerView givenView = ResponseTrackerView.builder()
                .id(givenId)
                .build();

        final Optional<Long> optionalActual = givenView.findId();
        assertTrue(optionalActual.isPresent());
        final Long actual = optionalActual.get();
        assertSame(givenId, actual);
    }
}
