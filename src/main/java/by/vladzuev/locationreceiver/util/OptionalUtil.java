package by.vladzuev.locationreceiver.util;

import lombok.experimental.UtilityClass;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@UtilityClass
public final class OptionalUtil {

    public static OptionalInt ofNullableInt(final Integer value) {
        return value != null ? OptionalInt.of(value) : OptionalInt.empty();
    }

    public static OptionalDouble ofNullableDouble(final Double value) {
        return value != null ? OptionalDouble.of(value) : OptionalDouble.empty();
    }
}
