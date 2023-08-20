package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Stream.empty;

@UtilityClass
public final class StreamUtil {

    public static <T> Stream<T> concat(final Stream<? extends T>... streams) {
        return streams != null
                ? Stream.of(streams).flatMap(identity())
                : empty();
    }

}
