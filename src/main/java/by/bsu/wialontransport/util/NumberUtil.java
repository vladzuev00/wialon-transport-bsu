package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import static java.lang.Math.pow;

@UtilityClass
public final class NumberUtil {

    private static double round(final double source, final short precision) {
        final long factor = (long) pow(10, precision);
        return (double) Math.round(source * factor) / factor;
    }

}
