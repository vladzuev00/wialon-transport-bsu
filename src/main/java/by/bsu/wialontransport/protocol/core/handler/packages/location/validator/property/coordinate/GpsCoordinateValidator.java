package by.bsu.wialontransport.protocol.core.handler.packages.location.validator.property.coordinate;

import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.property.LocationPropertyValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class GpsCoordinateValidator extends LocationPropertyValidator<Double> {
    private final Double min;
    private final Double max;

    @Override
    protected final boolean isLessOrEqual(final Double first, final Double second) {
        return first.compareTo(second) <= 0;
    }

    @Override
    protected final boolean isBiggerOrEqual(final Double first, final Double second) {
        return first.compareTo(second) >= 0;
    }
}
