package by.bsu.wialontransport.protocol.core.handler.packages.location.validator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class LocationPropertyValidator<T> {

    public final boolean isValid(final T value) {
        final T minAllowable = getMinAllowable();
        final T maxAllowable = getMaxAllowable();
        return isMore(value, minAllowable) && isLess(value, maxAllowable);
    }

    protected abstract T getMinAllowable();

    protected abstract T getMaxAllowable();

    protected abstract boolean isLess(final T first, final T second);

    protected abstract boolean isMore(final T first, final T second);
}
