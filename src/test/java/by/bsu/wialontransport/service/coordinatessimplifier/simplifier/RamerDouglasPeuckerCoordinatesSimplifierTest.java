package by.bsu.wialontransport.service.coordinatessimplifier.simplifier;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.model.Coordinate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static by.bsu.wialontransport.util.CsvReadingTestUtil.readCoordinates;
import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
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
        final List<Coordinate> givenCoordinates = List.of(
                new Coordinate(1., 5.),
                new Coordinate(2., 3.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(11., 4.),
                new Coordinate(13., 3.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.)
        );

        final List<Coordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<Coordinate> expected = List.of(
                new Coordinate(1., 5.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void coordinatesWithDuplicationShouldBeSimplified() {
        final List<Coordinate> givenCoordinates = List.of(
                new Coordinate(1., 5.),
                new Coordinate(1., 5.),
                new Coordinate(2., 3.),
                new Coordinate(2., 3.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(11., 4.),
                new Coordinate(13., 3.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.),
                new Coordinate(18., 5.)
        );

        final List<Coordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<Coordinate> expected = List.of(
                new Coordinate(1., 5.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void nullCoordinatesShouldBeSimplified() {
        final List<Coordinate> actual = coordinatesSimplifier.simplify(null);
        assertNull(actual);
    }

    @Test
    public void emptyCoordinatesShouldBeSimplified() {
        final List<Coordinate> givenCoordinates = emptyList();

        final List<Coordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        assertSame(givenCoordinates, actual);
    }

    @Test
    public void twoCoordinatesShouldBeSimplified() {
        final List<Coordinate> givenCoordinates = List.of(
                new Coordinate(1., 5.),
                new Coordinate(5., 1.)
        );

        final List<Coordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        assertSame(givenCoordinates, actual);
    }

    @Test
    public void coordinatesBelongingOneStraightLineShouldBeSimplified() {
        final List<Coordinate> givenCoordinates = List.of(
                new Coordinate(1, 1),
                new Coordinate(2, 2),
                new Coordinate(3, 3),
                new Coordinate(4, 4),
                new Coordinate(5, 5),
                new Coordinate(6, 6),
                new Coordinate(7, 7)
        );

        final List<Coordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<Coordinate> expected = List.of(
                new Coordinate(1, 1),
                new Coordinate(7, 7)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void bigTrackShouldBeSimplified()
            throws Exception {
        final List<Coordinate> givenCoordinates = readCoordinates(FILE_PATH_COORDINATES_BEFORE);

        final List<Coordinate> actual = coordinatesSimplifier.simplify(givenCoordinates);
        final List<Coordinate> expected = readCoordinates(FILE_PATH_COORDINATES_AFTER);
        assertEquals(expected, actual);
    }

    private static double findEpsilon(final RamerDouglasPeuckerCoordinatesSimplifier simplifier) {
        return findProperty(simplifier, FIELD_NAME_EPSILON, Double.class);
    }
}
