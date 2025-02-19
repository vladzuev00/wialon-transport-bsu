package by.vladzuev.locationreceiver.util;

import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@UtilityClass
public final class CoordinateUtil {

    public static Point jtsPoint(final GeometryFactory factory, final GpsCoordinate coordinate) {
        return factory.createPoint(new Coordinate(coordinate.getLongitude(), coordinate.getLatitude()));
    }
}
