package by.bsu.wialontransport.service.onlinechecking;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.online.TrackerOnline;
import by.bsu.wialontransport.model.online.TrackerOnline.LastData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static by.bsu.wialontransport.model.online.OnlineStatus.OFFLINE;
import static by.bsu.wialontransport.model.online.OnlineStatus.ONLINE;
import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public final class TrackerOnlineServiceTest extends AbstractSpringBootTest {

    @MockBean
    private DataService mockedDataService;

    @Autowired
    private TrackerOnlineService service;

    @Test
    public void trackerShouldBeOnline() {
        final Tracker givenTracker = createTracker(255L);
        final LocalDateTime givenLastDataDateTime = now().minusSeconds(295);
        final Coordinate givenLastDataCoordinate = new Coordinate(5.5, 6.6);

        final Data givenLastData = createData(givenLastDataDateTime, givenLastDataCoordinate);
        when(mockedDataService.findTrackerLastData(same(givenTracker))).thenReturn(Optional.of(givenLastData));

        final TrackerOnline actual = service.check(givenTracker);
        final TrackerOnline expected = TrackerOnline.builder()
                .tracker(givenTracker)
                .lastData(new LastData(givenLastDataDateTime, givenLastDataCoordinate))
                .status(ONLINE)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeOfflineBecauseOfThresholdExceeded() {
        final Tracker givenTracker = createTracker(255L);
        final LocalDateTime givenLastDataDateTime = now().minusSeconds(305);
        final Coordinate givenLastDataCoordinate = new Coordinate(5.5, 6.6);

        final Data givenLastData = createData(givenLastDataDateTime, givenLastDataCoordinate);
        when(mockedDataService.findTrackerLastData(same(givenTracker))).thenReturn(Optional.of(givenLastData));

        final TrackerOnline actual = service.check(givenTracker);
        final TrackerOnline expected = TrackerOnline.builder()
                .tracker(givenTracker)
                .lastData(new LastData(givenLastDataDateTime, givenLastDataCoordinate))
                .status(OFFLINE)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeOfflineBecauseOfThereIsNotDataFromGivenTracker() {
        final Tracker givenTracker = createTracker(255L);

        when(mockedDataService.findTrackerLastData(same(givenTracker))).thenReturn(empty());

        final TrackerOnline actual = service.check(givenTracker);
        final TrackerOnline expected = TrackerOnline.builder()
                .tracker(givenTracker)
                .status(OFFLINE)
                .build();
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static Data createData(final LocalDateTime dateTime, final Coordinate coordinate) {
        return Data.builder()
                .dateTime(dateTime)
                .coordinate(coordinate)
                .build();
    }
}
