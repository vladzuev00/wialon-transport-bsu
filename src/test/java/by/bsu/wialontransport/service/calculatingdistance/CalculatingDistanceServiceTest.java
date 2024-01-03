package by.bsu.wialontransport.service.calculatingdistance;

import by.bsu.wialontransport.model.Coordinate;
import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.util.CsvReadingTestUtil.readCoordinates;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public final class CalculatingDistanceServiceTest {
    private static final String FILE_PATH_WITH_COORDINATES = "./src/test/resources/distancecalculating/coordinates.csv";

    private final CalculatingDistanceService service = new CalculatingDistanceService();

    @Test
    public void twoCoordinatesDistanceShouldBeCalculated() {
        final Coordinate firstGivenCoordinate = new Coordinate(55.534343, 23.54545);
        final Coordinate secondGivenCoordinate = new Coordinate(55.554344, 23.57544);

        final double actual = service.calculate(firstGivenCoordinate, secondGivenCoordinate);
        final double expected = 2.919738716964184;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void bigDistanceShouldBeCalculated()
            throws Exception {
        final List<Coordinate> givenCoordinates = readCoordinates(FILE_PATH_WITH_COORDINATES);

        final double actual = service.calculate(givenCoordinates);
        final double expected = 2231133.2720121145;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void distanceCalculatedByZeroCoordinatesShouldBeEqualZero() {
        final List<Coordinate> givenCoordinates = emptyList();

        final double actual = service.calculate(givenCoordinates);
        final double expected = 0.;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void distanceCalculatedByOneCoordinatesShouldBeEqualZero() {
        final List<Coordinate> givenCoordinates = List.of(new Coordinate(55.534343, 23.54545));

        final double actual = service.calculate(givenCoordinates);
        final double expected = 0.;
        assertEquals(expected, actual, 0.);
    }

}
