package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.mileage.MileageCalculatingService;
import by.bsu.wialontransport.service.report.model.TrackerMovement;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class UserMovementReportBuildingContextFactoryTest {

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private DataService mockedDataService;

    @Mock
    private TrackFactory mockedTrackFactory;

    @Mock
    private MileageCalculatingService mockedMileageCalculatingService;

    private UserMovementReportBuildingContextFactory contextFactory;

    @Before
    public void initializeContextFactory() {
        this.contextFactory = new UserMovementReportBuildingContextFactory(
                this.mockedTrackerService,
                this.mockedDataService,
                this.mockedTrackFactory,
                this.mockedMileageCalculatingService
        );
    }

    @Test
    public void contextShouldBeCreated() {
        final User givenUser = createUser(255L);
        final DateInterval givenDateInterval = new DateInterval(
                LocalDate.of(2023, 7, 13),
                LocalDate.of(2023, 7, 14)
        );

        final Tracker firstGivenTracker = createTracker(256L, "11112222333344445556");
        final Tracker secondGivenTracker = createTracker(257L, "11112222333344445554");
        final Tracker thirdGivenTracker = createTracker(258L, "11112222333344445555");
        final List<Tracker> givenTrackers = List.of(firstGivenTracker, secondGivenTracker, thirdGivenTracker);

        final Data firstGivenData = createData(
                255L,
                LocalDateTime.of(2023, 7, 13, 1, 0, 2),
                firstGivenTracker
        );
        final Data secondGivenData = createData(
                256L,
                LocalDateTime.of(2023, 7, 13, 1, 0, 0),
                firstGivenTracker
        );
        final Data thirdGivenData = createData(
                257L,
                LocalDateTime.of(2023, 7, 13, 1, 0, 1),
                secondGivenTracker
        );
        final List<Data> givenData = List.of(firstGivenData, secondGivenData, thirdGivenData);
        when(this.mockedDataService.findDataWithTrackerAndAddress(same(givenUser), same(givenDateInterval)))
                .thenReturn(givenData);

        when(this.mockedTrackerService.findByUser(same(givenUser))).thenReturn(givenTrackers);

        final List<Data> expectedFirstTrackerData = List.of(secondGivenData, firstGivenData);
        final Track givenFirstTrackerTrack = new Track(
                List.of(new Coordinate(1.1, 2.2), new Coordinate(3.3, 4.4))
        );
        when(this.mockedTrackFactory.create(eq(expectedFirstTrackerData))).thenReturn(givenFirstTrackerTrack);

        final Mileage givenFirstTrackerMileage = new Mileage(1., 2.);
        when(this.mockedMileageCalculatingService.calculate(same(givenFirstTrackerTrack)))
                .thenReturn(givenFirstTrackerMileage);

        final List<Data> expectedSecondTrackerData = List.of(thirdGivenData);
        final Track givenSecondTrackerTrack = new Track(
                singletonList(new Coordinate(5.5, 6.6))
        );
        when(this.mockedTrackFactory.create(eq(expectedSecondTrackerData))).thenReturn(givenSecondTrackerTrack);

        final Mileage givenSecondTrackerMileage = new Mileage(0., 0.);
        when(this.mockedMileageCalculatingService.calculate(same(givenSecondTrackerTrack)))
                .thenReturn(givenSecondTrackerMileage);

        final List<Data> expectedThirdTrackerData = emptyList();
        final Track givenThirdTrackerTrack = new Track(emptyList());
        when(this.mockedTrackFactory.create(eq(expectedThirdTrackerData))).thenReturn(givenThirdTrackerTrack);

        final Mileage givenThirdTrackerMileage = new Mileage(0., 0.);
        when(this.mockedMileageCalculatingService.calculate(same(givenThirdTrackerTrack)))
                .thenReturn(givenThirdTrackerMileage);

        final UserMovementReportBuildingContext createdContext = this.contextFactory.create(
                givenUser, givenDateInterval
        );

        assertSame(givenUser, createdContext.getUser());
        assertSame(givenDateInterval, createdContext.getDateInterval());
        assertNotNull(createdContext.getDocument());
        assertNotNull(createdContext.getFont());
        assertNotNull(createdContext.getFontSize());
        assertNotNull(createdContext.getBorderColor());

        final Set<TrackerMovement> expectedTrackerMovements = Set.of(
                new TrackerMovement(firstGivenTracker, expectedFirstTrackerData, givenFirstTrackerMileage),
                new TrackerMovement(secondGivenTracker, expectedSecondTrackerData, givenSecondTrackerMileage),
                new TrackerMovement(thirdGivenTracker, expectedThirdTrackerData, givenThirdTrackerMileage)
        );
        final Set<TrackerMovement> actualTrackerMovementsAsSet = new HashSet<>(createdContext.getTrackerMovements());
        assertEquals(expectedTrackerMovements, actualTrackerMovementsAsSet);
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }

    private static Data createData(final Long id, final LocalDateTime dateTime, final Tracker tracker) {
        final LocalDate date = dateTime.toLocalDate();
        final LocalTime time = dateTime.toLocalTime();
        return Data.builder()
                .id(id)
                .date(date)
                .time(time)
                .tracker(tracker)
                .build();
    }

    private static Tracker createTracker(final Long id, final String imei) {
        return Tracker.builder()
                .id(id)
                .imei(imei)
                .build();
    }

}
