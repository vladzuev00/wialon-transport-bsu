package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.deepEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

@UtilityClass
public final class GeometryTestUtil {

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

    public static PreparedGeometry createPreparedPolygon(final GeometryFactory geometryFactory,
                                                         final double firstLongitude, final double firstLatitude,
                                                         final double secondLongitude, final double secondLatitude,
                                                         final double thirdLongitude, final double thirdLatitude,
                                                         final double fourthLongitude, final double fourthLatitude) {
        return prepare(
                createPolygon(
                        geometryFactory,
                        firstLongitude,
                        firstLatitude,
                        secondLongitude,
                        secondLatitude,
                        thirdLongitude,
                        thirdLatitude,
                        fourthLongitude,
                        fourthLatitude
                )
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

    public static LineString createLineString(final GeometryFactory geometryFactory,
                                              final double firstLongitude, final double firstLatitude,
                                              final double secondLongitude, final double secondLatitude,
                                              final double thirdLongitude, final double thirdLatitude) {
        return geometryFactory.createLineString(new Coordinate[]{
                new CoordinateXY(firstLongitude, firstLatitude),
                new CoordinateXY(secondLongitude, secondLatitude),
                new CoordinateXY(thirdLongitude, thirdLatitude)
        });
    }

    public static void checkEquals(org.wololo.geojson.Polygon expected, org.wololo.geojson.Polygon actual) {
        assertArrayEquals(expected.getBbox(), actual.getBbox(), 0.);
        assertTrue(deepEquals(expected.getCoordinates(), actual.getCoordinates()));
    }
}
