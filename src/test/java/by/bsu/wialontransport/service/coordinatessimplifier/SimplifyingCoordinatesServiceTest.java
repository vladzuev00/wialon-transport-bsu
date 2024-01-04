package by.bsu.wialontransport.service.coordinatessimplifier;

import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.coordinatessimplifier.simplifier.CoordinatesSimplifier;
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
        final Coordinate firstGivenCoordinate = new Coordinate(1, 1);
        final Coordinate secondGivenCoordinate = new Coordinate(2, 2);
        final Coordinate thirdGivenCoordinate = new Coordinate(3, 3);
        final List<Coordinate> givenCoordinates = List.of(
                firstGivenCoordinate,
                secondGivenCoordinate,
                thirdGivenCoordinate
        );

        final List<Coordinate> givenSimplifiedCoordinates = List.of(firstGivenCoordinate, thirdGivenCoordinate);
        when(mockedCoordinatesSimplifier.simplify(same(givenCoordinates))).thenReturn(givenSimplifiedCoordinates);

        final List<Coordinate> actual = service.simplify(givenCoordinates);
        assertSame(givenSimplifiedCoordinates, actual);
    }

}
