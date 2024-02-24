package by.bsu.wialontransport.model.online;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.online.TrackerOnlineInfo.LastData;
import org.junit.Test;

import java.util.Optional;

import static by.bsu.wialontransport.model.online.OnlineStatus.OFFLINE;
import static by.bsu.wialontransport.model.online.TrackerOnlineInfo.createNoDataInfo;
import static java.time.LocalDateTime.now;
import static org.junit.Assert.*;

public final class TrackerOnlineInfoTest {

    @Test
    public void noDataInfoShouldBeCreated() {
        final Tracker givenTracker = createTracker(255L);

        final TrackerOnlineInfo actual = createNoDataInfo(givenTracker);
        final TrackerOnlineInfo expected = TrackerOnlineInfo.builder()
                .tracker(givenTracker)
                .status(OFFLINE)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void lastDataShouldBeGot() {
        final LastData givenLastData = new LastData(now(), new Coordinate(1, 2));
        final TrackerOnlineInfo givenInfo = TrackerOnlineInfo.builder()
                .lastData(givenLastData)
                .build();

        final Optional<LastData> optionalActual = givenInfo.getLastData();
        assertTrue(optionalActual.isPresent());
        final LastData actual = optionalActual.get();
        assertSame(givenLastData, actual);
    }

    @Test
    public void lastDataShouldNotBeGot() {
        final TrackerOnlineInfo givenInfo = TrackerOnlineInfo.builder().build();

        final Optional<LastData> optionalActual = givenInfo.getLastData();
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }
}
