package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static by.bsu.wialontransport.util.CollectionTestUtil.createLinkedHashMap;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class UserMovementReportBuildingContextFactoryTest {

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private DataService mockedDataService;

    private UserMovementReportBuildingContextFactory contextFactory;

    @Before
    public void initializeContextFactory() {
        this.contextFactory = new UserMovementReportBuildingContextFactory(
                this.mockedTrackerService, this.mockedDataService
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

        final Data firstGivenFoundData = createData(255L, firstGivenTracker);
        final Data secondGivenFoundData = createData(256L, firstGivenTracker);
        final Data thirdGivenFoundData = createData(257L, secondGivenTracker);

        final List<Data> givenFoundData = List.of(firstGivenFoundData, secondGivenFoundData, thirdGivenFoundData);
        when(this.mockedDataService.findDataWithTrackerAndAddress(same(givenUser), same(givenDateInterval)))
                .thenReturn(givenFoundData);

        when(this.mockedTrackerService.findByUser(same(givenUser)))
                .thenReturn(List.of(firstGivenTracker, secondGivenTracker, thirdGivenTracker));

        final UserMovementReportBuildingContext createdContext = this.contextFactory.create(
                givenUser, givenDateInterval
        );

        assertSame(givenUser, createdContext.getUser());
        assertSame(givenDateInterval, createdContext.getDateInterval());
        assertNotNull(createdContext.getDocument());
        assertNotNull(createdContext.getFont());

        final Map<Tracker, Integer> expectedPointCountsByAllTrackers = createLinkedHashMap(
                secondGivenTracker, 1,
                firstGivenTracker, 2,
                thirdGivenTracker, 0
        );
        assertEquals(expectedPointCountsByAllTrackers, createdContext.getPointCountsByAllTrackers());

        final Map<Tracker, List<Data>> expectedDataGroupedBySortedByImeiTrackers = createLinkedHashMap(
                secondGivenTracker, List.of(thirdGivenFoundData),
                firstGivenTracker, List.of(firstGivenFoundData, secondGivenFoundData)
        );
        assertEquals(expectedDataGroupedBySortedByImeiTrackers, createdContext.getDataBySortedByImeiTrackers());
    }

    @Test
    public void a() {
        throw new RuntimeException();
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }

    private static Data createData(final Long id, final Tracker tracker) {
        return Data.builder()
                .id(id)
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
