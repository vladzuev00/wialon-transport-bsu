package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property;

import by.vladzuev.locationreceiver.crud.dto.Location;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class LocationPropertyValidator<T> {

    public final boolean isValid(final Location location) {
        final T value = getValue(location);
        final T min = getMin();
        final T max = getMax();
        return isBiggerOrEqual(value, min) && isLessOrEqual(value, max);
    }

    protected abstract T getValue(final Location location);

    protected abstract T getMin();

    protected abstract T getMax();

    protected abstract boolean isLessOrEqual(final T first, final T second);

    protected abstract boolean isBiggerOrEqual(final T first, final T second);
}
