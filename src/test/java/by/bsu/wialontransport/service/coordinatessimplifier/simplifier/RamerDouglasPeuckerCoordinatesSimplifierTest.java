package by.bsu.wialontransport.service.coordinatessimplifier.simplifier;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.model.GpsCoordinate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static by.bsu.wialontransport.util.CsvReadingTestUtil.readCoordinates;
import static by.bsu.wialontransport.util.ReflectionUtil.getProperty;
import static java.io.File.separator;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public final class RamerDouglasPeuckerCoordinatesSimplifierTest extends AbstractSpringBootTest {
    private static final String FIELD_NAME_EPSILON = "epsilon";

    private static final String FOLDER_PATH = "./src/test/resources/coordinates-simplifying";

    private static final String FILE_NAME_COORDINATES_BEFORE = "before.csv";
    private static final String FILE_PATH_COORDINATES_BEFORE = FOLDER_PATH + separator + FILE_NAME_COORDINATES_BEFORE;

    private static final String FILE_NAME_COORDINATES_AFTER = "after.csv";
    private static final String FILE_PATH_COORDINATES_AFTER = FOLDER_PATH + separator + FILE_NAME_COORDINATES_AFTER;

    @Value("#{new Double('${track-simplifier.ramer-douglas-peucker.epsilon}')}")
    private double givenEpsilon;

    @Autowired
    private RamerDouglasPeuckerCoordinatesSimplifier coordinatesSimplifier;

    @Test
    public void simplifierShouldBeCreated() {
        final double actual = findEpsilon(coordinatesSimplifier);
        assertEquals(givenEpsilon, actual, 0.);
    }

    @Test(expected = IllegalArgumentException.class)
    public void simplifierShouldNotBeCreatedBecauseOfNotValidEpsilon() {
        final double givenEpsilon = 0;

        new RamerDouglasPeuckerCoordinatesSimplifier(givenEpsilon);
    }

    @Test
    public void coordinatesShouldBeSimplified() {
        final List<GpsCoordinate> givenCoordinates = List.of(
                new GpsCoordinate(1., 5.),
                new GpsCoordinate(2., 3.),
                new GpsCoordinate(5., 1.),
                new GpsCoordinate(6., 4.),
                new GpsCoordinate(9., 6.),
                new GpsCoordinate(11., 4.),
                new GpsCoordinate(13., 3.),
                new GpsCoordinate(14., 2.),
                new GpsCoordinate(18., 5.)
        );

        final List<GpsCoordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<GpsCoordinate> expected = List.of(
                new GpsCoordinate(1., 5.),
                new GpsCoordinate(5., 1.),
                new GpsCoordinate(6., 4.),
                new GpsCoordinate(9., 6.),
                new GpsCoordinate(14., 2.),
                new GpsCoordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void coordinatesWithDuplicationShouldBeSimplified() {
        final List<GpsCoordinate> givenCoordinates = List.of(
                new GpsCoordinate(1., 5.),
                new GpsCoordinate(1., 5.),
                new GpsCoordinate(2., 3.),
                new GpsCoordinate(2., 3.),
                new GpsCoordinate(5., 1.),
                new GpsCoordinate(6., 4.),
                new GpsCoordinate(9., 6.),
                new GpsCoordinate(11., 4.),
                new GpsCoordinate(13., 3.),
                new GpsCoordinate(14., 2.),
                new GpsCoordinate(18., 5.),
                new GpsCoordinate(18., 5.)
        );

        final List<GpsCoordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<GpsCoordinate> expected = List.of(
                new GpsCoordinate(1., 5.),
                new GpsCoordinate(5., 1.),
                new GpsCoordinate(6., 4.),
                new GpsCoordinate(9., 6.),
                new GpsCoordinate(14., 2.),
                new GpsCoordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void nullCoordinatesShouldBeSimplified() {
        final List<GpsCoordinate> actual = coordinatesSimplifier.simplify(null);
        assertNull(actual);
    }

    @Test
    public void emptyCoordinatesShouldBeSimplified() {
        final List<GpsCoordinate> givenCoordinates = emptyList();

        final List<GpsCoordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        assertSame(givenCoordinates, actual);
    }

    @Test
    public void twoCoordinatesShouldBeSimplified() {
        final List<GpsCoordinate> givenCoordinates = List.of(
                new GpsCoordinate(1., 5.),
                new GpsCoordinate(5., 1.)
        );

        final List<GpsCoordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        assertSame(givenCoordinates, actual);
    }

    @Test
    public void coordinatesBelongingOneStraightLineShouldBeSimplified() {
        final List<GpsCoordinate> givenCoordinates = List.of(
                new GpsCoordinate(1, 1),
                new GpsCoordinate(2, 2),
                new GpsCoordinate(3, 3),
                new GpsCoordinate(4, 4),
                new GpsCoordinate(5, 5),
                new GpsCoordinate(6, 6),
                new GpsCoordinate(7, 7)
        );

        final List<GpsCoordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<GpsCoordinate> expected = List.of(
                new GpsCoordinate(1, 1),
                new GpsCoordinate(7, 7)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void bigTrackShouldBeSimplified()
            throws Exception {
        final List<GpsCoordinate> givenCoordinates = readCoordinates(FILE_PATH_COORDINATES_BEFORE);

        final List<GpsCoordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<GpsCoordinate> expected = readCoordinates(FILE_PATH_COORDINATES_AFTER);
        assertEquals(expected, actual);
    }

    private static double findEpsilon(final RamerDouglasPeuckerCoordinatesSimplifier simplifier) {
        return getProperty(simplifier, FIELD_NAME_EPSILON, Double.class);
    }
}
