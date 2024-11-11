package by.bsu.wialontransport.service.searchingcities.areaiterator;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.GpsCoordinate;
import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.collectToList;
import static by.bsu.wialontransport.util.ReflectionUtil.getProperty;
import static org.junit.Assert.assertEquals;

public final class AreaIteratorTest {
    private static final String FIELD_NAME_CURRENT_COORDINATE = "currentCoordinate";

    @Test
    public void currentCoordinateShouldBeInitiallyInitialized() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new GpsCoordinate(5., 5.),
                new GpsCoordinate(6.2, 6.2)
        );
        final double givenSearchStep = 0.5;
        final AreaIterator givenIterator = new AreaIterator(givenAreaCoordinate, givenSearchStep);

        final GpsCoordinate actual = findCurrentCoordinate(givenIterator);
        final GpsCoordinate expected = new GpsCoordinate(4.5, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void areaShouldBeIterated() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new GpsCoordinate(5., 5.),
                new GpsCoordinate(6.2, 6.2)
        );
        final double givenSearchStep = 0.5;
        final AreaIterator givenAreaIterator = new AreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<GpsCoordinate> actual = collectToList(givenAreaIterator);
        final List<GpsCoordinate> expected = List.of(
                new GpsCoordinate(5., 5.),
                new GpsCoordinate(5.5, 5.),
                new GpsCoordinate(6., 5.),

                new GpsCoordinate(5., 5.5),
                new GpsCoordinate(5.5, 5.5),
                new GpsCoordinate(6., 5.5),

                new GpsCoordinate(5., 6.),
                new GpsCoordinate(5.5, 6.),
                new GpsCoordinate(6., 6.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void areaShouldBeIteratedByOneCoordinateBecauseOfSearchStepIsBigForGivenArea() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new GpsCoordinate(5., 5.),
                new GpsCoordinate(6.2, 6.2)
        );
        final double givenSearchStep = 1.5;
        final AreaIterator givenAreaIterator = new AreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<GpsCoordinate> actual = collectToList(givenAreaIterator);
        final List<GpsCoordinate> expected = List.of(new GpsCoordinate(5., 5.));
        assertEquals(expected, actual);
    }

    private static GpsCoordinate findCurrentCoordinate(final AreaIterator iterator) {
        return getProperty(iterator, FIELD_NAME_CURRENT_COORDINATE, GpsCoordinate.class);
    }
}
