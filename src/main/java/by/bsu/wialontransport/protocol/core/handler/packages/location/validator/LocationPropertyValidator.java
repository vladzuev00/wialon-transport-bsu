package by.bsu.wialontransport.protocol.core.handler.packages.location.validator;

import by.bsu.wialontransport.crud.dto.Location;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class LocationPropertyValidator<T> {

    public final boolean isValid(final Location location) {
        final T value = getValue(location);
        final T minAllowable = getMinAllowable();
        final T maxAllowable = getMaxAllowable();
        return isBiggerOrEqual(value, minAllowable) && isLessOrEqual(value, maxAllowable);
    }

    protected abstract T getValue(final Location location);

    protected abstract T getMinAllowable();

    protected abstract T getMaxAllowable();

    protected abstract boolean isLessOrEqual(final T first, final T second);

    protected abstract boolean isBiggerOrEqual(final T first, final T second);
}
