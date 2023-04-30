package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.deepEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

@UtilityClass
public final class GeometryUtil {

    public static Geometry createPolygon(final GeometryFactory geometryFactory,
                                         final double firstLongitude, final double firstLatitude,
                                         final double secondLongitude, final double secondLatitude,
                                         final double thirdLongitude, final double thirdLatitude) {
        return createPolygon(
                geometryFactory,
                new CoordinateXY(firstLongitude, firstLatitude),
                new CoordinateXY(secondLongitude, secondLatitude),
                new CoordinateXY(thirdLongitude, thirdLatitude)
        );
    }

    public static Geometry createPolygon(final GeometryFactory geometryFactory,
                                         final double firstLongitude, final double firstLatitude,
                                         final double secondLongitude, final double secondLatitude,
                                         final double thirdLongitude, final double thirdLatitude,
                                         final double fourthLongitude, final double fourthLatitude) {
        return createPolygon(
                geometryFactory,
                new CoordinateXY(firstLongitude, firstLatitude),
                new CoordinateXY(secondLongitude, secondLatitude),
                new CoordinateXY(thirdLongitude, thirdLatitude),
                new CoordinateXY(fourthLongitude, fourthLatitude)
        );
    }

    public static Geometry createPolygon(final GeometryFactory geometryFactory, final CoordinateXY... coordinates) {
        final CoordinateXY[] boundedCoordinates = copyOf(coordinates, coordinates.length + 1);
        boundedCoordinates[boundedCoordinates.length - 1] = coordinates[0];
        return geometryFactory.createPolygon(boundedCoordinates);
    }

    public static Point createPoint(final GeometryFactory geometryFactory,
                                    final double longitude, final double latitude) {
        final CoordinateXY coordinate = new CoordinateXY(longitude, latitude);
        return geometryFactory.createPoint(coordinate);
    }

    public static void checkEquals(org.wololo.geojson.Polygon expected, org.wololo.geojson.Polygon actual) {
        assertArrayEquals(expected.getBbox(), actual.getBbox(), 0.);
        assertTrue(deepEquals(expected.getCoordinates(), actual.getCoordinates()));
    }
}
