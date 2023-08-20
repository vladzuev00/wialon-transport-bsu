package by.bsu.wialontransport.util;

import by.bsu.wialontransport.model.Coordinate;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CoordinateUtil {

    public static org.locationtech.jts.geom.Coordinate mapToJtsCoordinate(final Coordinate mapped) {
        final Double longitude = mapped.getLongitude();
        final Double latitude = mapped.getLatitude();
        return new org.locationtech.jts.geom.Coordinate(longitude, latitude);
    }

    public static Coordinate mapToCoordinate(final org.locationtech.jts.geom.Coordinate mapped) {
        final double latitude = mapped.getY();
        final double longitude = mapped.getX();
        return new Coordinate(latitude, longitude);
    }

}
