package by.bsu.wialontransport.protocol.core.handler.packages.location.validator.coordinate;

import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationPropertyValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class GpsCoordinateValidator extends LocationPropertyValidator<Double> {
    private final Double minAllowable;
    private final Double maxAllowable;
}
