package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property.LocationPropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class LocationValidator {
    private final List<LocationPropertyValidator<?>> propertyValidators;

    public boolean isValid(final Location location) {
        return propertyValidators.stream().allMatch(validator -> validator.isValid(location));
    }
}
