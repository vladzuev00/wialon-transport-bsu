package by.bsu.wialontransport.protocol.core.handler.packages.location.validator;

import by.bsu.wialontransport.crud.dto.Location;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class LocationPropertyValidator<T extends Comparable<T>> {

    public final boolean isValid(final Location location) {
        final T value = getValue(location);
        final T minAllowable = getMinAllowable();
        final T maxAllowable = getMaxAllowable();
        return value.compareTo(minAllowable) >= 0 && value.compareTo(maxAllowable) <= 0;
    }

    protected abstract T getValue(final Location location);

    protected abstract T getMinAllowable();

    protected abstract T getMaxAllowable();
}
