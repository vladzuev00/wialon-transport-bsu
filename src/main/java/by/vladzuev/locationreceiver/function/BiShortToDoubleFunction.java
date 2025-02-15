package by.vladzuev.locationreceiver.function;

@FunctionalInterface
public interface BiShortToDoubleFunction {
    double apply(final short first, final short second);
}
