package by.bsu.wialontransport.service.searchingcities.areaiterator;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.RequestCoordinate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class AreaIteratorTest {

    @Test
    public void areaShouldBeIterated() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new RequestCoordinate(5., 5.),
                new RequestCoordinate(6.2, 6.2)
        );
        final double givenSearchStep = 0.5;
        final AreaIterator givenAreaIterator = new AreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<RequestCoordinate> actual = iterate(givenAreaIterator);
        final List<RequestCoordinate> expected = List.of(
                new RequestCoordinate(5., 5.),
                new RequestCoordinate(5.5, 5.),
                new RequestCoordinate(6., 5.),

                new RequestCoordinate(5., 5.5),
                new RequestCoordinate(5.5, 5.5),
                new RequestCoordinate(6., 5.5),

                new RequestCoordinate(5., 6.),
                new RequestCoordinate(5.5, 6.),
                new RequestCoordinate(6., 6.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void areaShouldBeIteratedByOnePointsBecauseOfSearchStepIsBigForGivenArea() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new RequestCoordinate(5., 5.),
                new RequestCoordinate(6.2, 6.2)
        );
        final double givenSearchStep = 1.5;
        final AreaIterator givenAreaIterator = new AreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<RequestCoordinate> actual = iterate(givenAreaIterator);
        final List<RequestCoordinate> expected = List.of(
                new RequestCoordinate(5., 5.)
        );
        assertEquals(expected, actual);
    }

    private static List<RequestCoordinate> iterate(final Iterator<RequestCoordinate> iterator) {
        final List<RequestCoordinate> coordinates = new ArrayList<>();
        iterator.forEachRemaining(coordinates::add);
        return coordinates;
    }
}
