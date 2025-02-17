package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property.coordinate;

import by.vladzuev.locationreceiver.crud.dto.Location;
import org.springframework.stereotype.Component;

@Component
public final class LongitudeValidator extends GpsCoordinateValidator {
    private static final Double MIN = -180.;
    private static final Double MAX = 180.;

    public LongitudeValidator() {
        super(MIN, MAX);
    }

    @Override
    protected Double getValue(final Location location) {
        return location.getCoordinate().getLongitude();
    }
}
