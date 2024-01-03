package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import static java.lang.Double.compare;
import static java.lang.Math.pow;

@UtilityClass
public final class NumberUtil {

    public static double round(final double source, final short precision) {
        final long factor = (long) pow(10, precision);
        return (double) Math.round(source * factor) / factor;
    }

    public static double createDoubleByParts(final int integerPart, final int fractionalPart) {
        final double zeroWithFractionalPart = createZeroWithFractionalPart(fractionalPart);
        return integerPart + zeroWithFractionalPart;
    }

    public static double createZeroWithFractionalPart(final int fractionalPart) {
        double resultValue = fractionalPart;
        while (compare(resultValue, 1) >= 0) {
            resultValue /= 10;
        }
        return resultValue;
    }

    public static boolean isPositive(final double value) {
        return compare(value, 0) > 0;
    }
}
