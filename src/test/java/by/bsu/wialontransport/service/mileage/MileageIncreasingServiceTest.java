package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerMileageService;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.mileage.calculator.MileageCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class MileageIncreasingServiceTest {

    @Mock
    private LocationService mockedDataService;

    @Mock
    private MileageCalculator mockedMileageCalculator;

    @Mock
    private TrackerMileageService mockedTrackerMileageService;

    private MileageIncreasingService mileageIncreasingService;

    @Before
    public void initializeMileageIncreasingService() {
        mileageIncreasingService = new MileageIncreasingService(
                mockedDataService,
                mockedMileageCalculator,
                mockedTrackerMileageService
        );
    }

    @Test
    public void mileageShouldBeIncreasedInCaseExistingLastData() {
        final Tracker givenTracker = createTracker(255L);

        final GpsCoordinate firstGivenCoordinate = new GpsCoordinate(1.1, 1.1);
        final GpsCoordinate secondGivenCoordinate = new GpsCoordinate(2.2, 2.2);
        final GpsCoordinate thirdGivenCoordinate = new GpsCoordinate(3.3, 3.3);
        final List<GpsCoordinate> givenCoordinates = List.of(
                firstGivenCoordinate,
                secondGivenCoordinate,
                thirdGivenCoordinate
        );

        final Track givenTrack = new Track(givenTracker, givenCoordinates);

        final GpsCoordinate givenLastDataCoordinate = new GpsCoordinate(4.4, 4.4);
        final Location givenLastData = createData(256L, givenLastDataCoordinate);
        when(mockedDataService.findLastLocationFetchingParameters(same(givenTracker)))
                .thenReturn(Optional.of(givenLastData));

        final List<GpsCoordinate> expectedPathCoordinates = List.of(
                givenLastDataCoordinate,
                firstGivenCoordinate,
                secondGivenCoordinate,
                thirdGivenCoordinate
        );
        final Mileage givenMileageDelta = new Mileage(10.1, 20.2);
        when(mockedMileageCalculator.calculate(eq(expectedPathCoordinates))).thenReturn(givenMileageDelta);

        mileageIncreasingService.increase(givenTrack);

        verify(mockedTrackerMileageService, times(1)).increaseMileage(
                same(givenTracker),
                same(givenMileageDelta)
        );
    }

    @Test
    public void mileageShouldBeIncreasedInCaseNotExistingLastData() {
        final Tracker givenTracker = createTracker(256L);

        final GpsCoordinate firstGivenCoordinate = new GpsCoordinate(1.1, 1.1);
        final GpsCoordinate secondGivenCoordinate = new GpsCoordinate(2.2, 2.2);
        final GpsCoordinate thirdGivenCoordinate = new GpsCoordinate(3.3, 3.3);
        final List<GpsCoordinate> givenCoordinates = List.of(
                firstGivenCoordinate,
                secondGivenCoordinate,
                thirdGivenCoordinate
        );

        final Track givenTrack = new Track(givenTracker, givenCoordinates);

        when(mockedDataService.findLastLocationFetchingParameters(same(givenTracker))).thenReturn(empty());

        final Mileage givenMileageDelta = new Mileage(10.1, 20.2);
        when(mockedMileageCalculator.calculate(same(givenCoordinates))).thenReturn(givenMileageDelta);

        mileageIncreasingService.increase(givenTrack);

        verify(mockedTrackerMileageService, times(1)).increaseMileage(
                same(givenTracker),
                same(givenMileageDelta)
        );
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Location createData(final Long id, final GpsCoordinate coordinate) {
        return Location.builder()
                .id(id)
                .coordinate(coordinate)
                .build();
    }
}
