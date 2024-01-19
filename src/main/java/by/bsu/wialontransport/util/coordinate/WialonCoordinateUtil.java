package by.bsu.wialontransport.util.coordinate;

import by.bsu.wialontransport.protocol.wialon.model.coordinate.GeographicCoordinate;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import lombok.experimental.UtilityClass;

import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere.SOUTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere.WESTERN;

//TODO: refactor
@UtilityClass
public final class WialonCoordinateUtil {

    public static double toDouble(final Latitude latitude) {
        final double value = latitude.getDegrees() + (latitude.getMinutes() / 60.) + (latitude.getMinuteShare() / 3600.);
        return latitude.getHemisphere() != SOUTH ? value : -1 * value;
    }

    public static double toDouble(final Longitude longitude) {
        final double value = longitude.getDegrees() + (longitude.getMinutes() / 60.) + (longitude.getMinuteShare() / 3600.);
        return longitude.getHemisphere() != WESTERN ? value : -1 * value;
    }
}
