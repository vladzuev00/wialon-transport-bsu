package by.bsu.wialontransport.service.calculatingdistance;

import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.util.CsvReadingTestUtil.readTrack;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public final class CalculatingDistanceServiceTest {
    private static final String FILE_PATH_WITH_TRACK = "./src/test/resources/distancecalculating/track.csv";

    private final CalculatingDistanceService service = new CalculatingDistanceService();

    @Test
    public void distanceBetweenTwoCoordinatesShouldBeCalculated() {
        final Coordinate firstGivenCoordinate = new Coordinate(55.534343, 23.54545);
        final Coordinate secondGivenCoordinate = new Coordinate(55.554344, 23.57544);

        final List<Coordinate> givenCoordinates = List.of(firstGivenCoordinate, secondGivenCoordinate);
        final Track givenTrack = new Track(givenCoordinates);

        final double actual = this.service.calculate(givenTrack);
        final double expected = 2.919738716964184;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void distanceCalculatedByZeroCoordinatesShouldBeEqualZero() {
        final List<Coordinate> givenCoordinates = emptyList();
        final Track givenTrack = new Track(givenCoordinates);

        final double actual = this.service.calculate(givenTrack);
        final double expected = 0.;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void distanceCalculatedByOneCoordinatesShouldBeEqualZero() {
        final List<Coordinate> givenCoordinates = List.of(new Coordinate(55.534343, 23.54545));
        final Track givenTrack = new Track(givenCoordinates);

        final double actual = this.service.calculate(givenTrack);
        final double expected = 0.;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void bigDistanceShouldBeCalculated()
            throws Exception {
        final Track givenTrack = readTrack(FILE_PATH_WITH_TRACK);

        final double actual = this.service.calculate(givenTrack);
        final double expected = 2231133.2720121145;
        assertEquals(expected, actual, 0.);
    }

}
