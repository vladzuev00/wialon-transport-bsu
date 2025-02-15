package by.vladzuev.locationreceiver.util;

import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Stream.empty;
import static java.util.stream.StreamSupport.stream;

@UtilityClass
public final class StreamUtil {

    public static <T> Stream<T> concat(final Stream<? extends T>... streams) {
        return streams != null
                ? Stream.of(streams).flatMap(identity())
                : empty();
    }

    public static boolean isEmpty(final Stream<?> stream) {
        return stream.findAny().isEmpty();
    }

    public static <T> Stream<T> toStream(final Iterator<T> iterator) {
        final Iterable<T> iterable = () -> iterator;
        final Spliterator<T> spliterator = iterable.spliterator();
        return stream(spliterator, false);
    }
}
