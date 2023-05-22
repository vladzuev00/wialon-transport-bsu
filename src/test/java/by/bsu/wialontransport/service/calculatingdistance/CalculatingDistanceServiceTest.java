package by.bsu.wialontransport.service.calculatingdistance;

import by.bsu.wialontransport.model.Coordinate;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public final class CalculatingDistanceServiceTest {

    private final CalculatingDistanceService service = new CalculatingDistanceService();

    @Test
    public void distanceBetweenTwoCoordinatesShouldBeCalculated() {
        final Coordinate firstGivenCoordinate = new Coordinate(55.534343, 23.54545);
        final Coordinate secondGivenCoordinate = new Coordinate(55.554344, 23.57544);

        final List<Coordinate> givenCoordinates = List.of(firstGivenCoordinate, secondGivenCoordinate);

        final double actual = this.service.calculate(givenCoordinates);
        final double expected = 2.919738716964184;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void distanceCalculatedByZeroCoordinatesShouldBeEqualZero() {
        final List<Coordinate> givenCoordinates = emptyList();

        final double actual = this.service.calculate(givenCoordinates);
        final double expected = 0.;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void distanceCalculatedByOneCoordinatesShouldBeEqualZero() {
        final List<Coordinate> givenCoordinates = List.of(new Coordinate(55.534343, 23.54545));

        final double actual = this.service.calculate(givenCoordinates);
        final double expected = 0.;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void bigDistanceShouldBeCalculated() {
        throw new RuntimeException();
    }

}
