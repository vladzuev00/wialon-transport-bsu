package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
        throw new RuntimeException();
    }

    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

}
