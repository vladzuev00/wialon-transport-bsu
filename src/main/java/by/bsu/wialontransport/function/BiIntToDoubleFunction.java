package by.bsu.wialontransport.function;

@FunctionalInterface
public interface BiIntToDoubleFunction {
    double apply(final int firstArgument, final int secondArgument);
}
