package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property;

import by.vladzuev.locationreceiver.protocol.core.property.LocationValidationProperty;
import by.vladzuev.locationreceiver.crud.dto.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SatelliteCountValidator extends LocationPropertyValidator<Integer> {
    private final LocationValidationProperty property;

    @Override
    protected Integer getValue(final Location location) {
        return location.getSatelliteCount();
    }

    @Override
    protected Integer getMin() {
        return property.getMinSatelliteCount();
    }

    @Override
    protected Integer getMax() {
        return property.getMaxSatelliteCount();
    }

    @Override
    protected boolean isLessOrEqual(final Integer first, final Integer second) {
        return first.compareTo(second) <= 0;
    }

    @Override
    protected boolean isBiggerOrEqual(final Integer first, final Integer second) {
        return first.compareTo(second) >= 0;
    }
}
