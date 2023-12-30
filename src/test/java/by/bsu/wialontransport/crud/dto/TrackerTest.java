package by.bsu.wialontransport.crud.dto;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class TrackerTest {

    @Test
    public void lastDataShouldBeFound() {
        final Data givenLastData = createData(255L);
        final Tracker givenTracker = createTracker(givenLastData);

        final Optional<Data> optionalActual = givenTracker.findLastData();
        assertTrue(optionalActual.isPresent());
        final Data actual = optionalActual.get();
        assertSame(givenLastData, actual);
    }

    @Test
    public void lastDataShouldNotBeFound() {
        final Tracker givenTracker = createTracker();

        final Optional<Data> optionalActual = givenTracker.findLastData();
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("SameParameterValue")
    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }

    private static Tracker createTracker(final Data lastData) {
        return Tracker.builder()
                .lastData(lastData)
                .build();
    }

    private static Tracker createTracker() {
        return Tracker.builder().build();
    }
}
