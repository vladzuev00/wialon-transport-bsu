package by.vladzuev.locationreceiver.function;

@FunctionalInterface
public interface ToShortFunction<T> {
    short apply(final T argument);
}
