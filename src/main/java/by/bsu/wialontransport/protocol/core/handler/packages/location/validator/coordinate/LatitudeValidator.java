package by.bsu.wialontransport.protocol.core.handler.packages.location.validator.coordinate;

import by.bsu.wialontransport.crud.dto.Location;
import org.springframework.stereotype.Component;

@Component
public final class LatitudeValidator extends GpsCoordinateValidator {
    private static final Double MIN = -90.;
    private static final Double MAX = 90.;

    public LatitudeValidator() {
        super(MIN, MAX);
    }

    @Override
    protected Double getValue(final Location location) {
        return location.getCoordinate().getLatitude();
    }
}
