package by.bsu.wialontransport.protocol.core.handler.packages.location.validator.coordinate;

import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationPropertyValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class GpsCoordinateValidator extends LocationPropertyValidator<Double> {
    private final Double minAllowable;
    private final Double maxAllowable;

    @Override
    protected final boolean isLessOrEqual(final Double first, final Double second) {
        return first.compareTo(second) <= 0;
    }

    @Override
    protected final boolean isBiggerOrEqual(final Double first, final Double second) {
        return first.compareTo(second) >= 0;
    }
}
