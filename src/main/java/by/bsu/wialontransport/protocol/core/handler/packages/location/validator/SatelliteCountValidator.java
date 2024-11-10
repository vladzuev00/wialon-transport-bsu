package by.bsu.wialontransport.protocol.core.handler.packages.location.validator;

import by.bsu.wialontransport.config.property.LocationValidationProperty;
import by.bsu.wialontransport.crud.dto.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SatelliteCountValidator extends LocationPropertyValidator<Integer> {
    private final LocationValidationProperty validationProperty;

    @Override
    protected Integer getValue(final Location location) {
        return location.getSatelliteCount();
    }

    @Override
    protected Integer getMinAllowable() {
        return validationProperty.getMinSatelliteCount();
    }

    @Override
    protected Integer getMaxAllowable() {
        return validationProperty.getGetMaxSatelliteCount();
    }
}
