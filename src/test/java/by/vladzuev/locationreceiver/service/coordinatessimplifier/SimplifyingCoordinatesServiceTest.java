package by.vladzuev.locationreceiver.service.coordinatessimplifier;

import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.coordinatessimplifier.simplifier.CoordinatesSimplifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SimplifyingCoordinatesServiceTest {

    @Mock
    private CoordinatesSimplifier mockedCoordinatesSimplifier;

    private SimplifyingCoordinatesService service;

    @Before
    public void initializeService() {
        service = new SimplifyingCoordinatesService(mockedCoordinatesSimplifier);
    }

    @Test
    public void coordinatesShouldBeSimplified() {
        final GpsCoordinate firstGivenCoordinate = new GpsCoordinate(1, 1);
        final GpsCoordinate secondGivenCoordinate = new GpsCoordinate(2, 2);
        final GpsCoordinate thirdGivenCoordinate = new GpsCoordinate(3, 3);
        final List<GpsCoordinate> givenCoordinates = List.of(
                firstGivenCoordinate,
                secondGivenCoordinate,
                thirdGivenCoordinate
        );

        final List<GpsCoordinate> givenSimplifiedCoordinates = List.of(firstGivenCoordinate, thirdGivenCoordinate);
        when(mockedCoordinatesSimplifier.simplify(same(givenCoordinates))).thenReturn(givenSimplifiedCoordinates);

        final List<GpsCoordinate> actual = service.simplify(givenCoordinates);
        assertSame(givenSimplifiedCoordinates, actual);
    }

}
