package by.vladzuev.locationreceiver.function;

@FunctionalInterface
public interface BiIntToDoubleFunction {
    double apply(final int firstArgument, final int secondArgument);
}
