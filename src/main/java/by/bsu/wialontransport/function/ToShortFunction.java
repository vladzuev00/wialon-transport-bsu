package by.bsu.wialontransport.function;

@FunctionalInterface
public interface ToShortFunction<T> {
    short apply(final T argument);
}
