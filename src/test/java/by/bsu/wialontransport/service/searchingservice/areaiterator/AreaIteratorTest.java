package by.bsu.wialontransport.service.searchingservice.areaiterator;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.searchingcities.areaiterator.AreaIterator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class AreaIteratorTest {

    @Test
    public void areaShouldBeIterated() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(5., 5.),
                new Coordinate(6.2, 6.2)
        );
        final double givenSearchStep = 0.5;
        final AreaIterator givenAreaIterator = new AreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<Coordinate> actual = iterate(givenAreaIterator);
        final List<Coordinate> expected = List.of(
                new Coordinate(5., 5.),
                new Coordinate(5.5, 5.),
                new Coordinate(6., 5.),

                new Coordinate(5., 5.5),
                new Coordinate(5.5, 5.5),
                new Coordinate(6., 5.5),

                new Coordinate(5., 6.),
                new Coordinate(5.5, 6.),
                new Coordinate(6., 6.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void areaShouldBeIteratedByOnePointsBecauseOfSearchStepIsBigForGivenArea() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(5., 5.),
                new Coordinate(6.2, 6.2)
        );
        final double givenSearchStep = 1.5;
        final AreaIterator givenAreaIterator = new AreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<Coordinate> actual = iterate(givenAreaIterator);
        final List<Coordinate> expected = List.of(
                new Coordinate(5., 5.)
        );
        assertEquals(expected, actual);
    }

    private static List<Coordinate> iterate(final Iterator<Coordinate> iterator) {
        final List<Coordinate> coordinates = new ArrayList<>();
        iterator.forEachRemaining(coordinates::add);
        return coordinates;
    }
}
