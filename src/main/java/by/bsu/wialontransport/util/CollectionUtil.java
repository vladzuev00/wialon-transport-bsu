package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@UtilityClass
public final class CollectionUtil {

    public static <S, K, V> Map<K, V> collectToTreeMap(final List<S> sources,
                                                       final Function<S, K> keyExtractor,
                                                       final Function<S, V> valueExtractor,
                                                       final Comparator<K> keyComparator) {
        return sources.stream()
                .collect(
                        toMap(
                                keyExtractor,
                                valueExtractor,
                                CollectionUtil::throwExceptionOnKeyDuplication,
                                () -> new TreeMap<>(keyComparator)
                        )
                );
    }

    public static <S, P> List<P> mapToList(final Collection<S> sources, final Function<S, P> elementMapper) {
        return mapToCollection(sources, elementMapper, Collectors::toUnmodifiableList);
    }

    public static <S, P> Set<P> mapToSet(final Collection<S> sources, final Function<S, P> elementMapper) {
        return mapToCollection(sources, elementMapper, Collectors::toUnmodifiableSet);
    }

    private static <V> V throwExceptionOnKeyDuplication(final V existing, final V replacement) {
        throw new IllegalArgumentException("Key duplication was found when collection to map");
    }

    private static <S, P, C extends Collection<P>> C mapToCollection(final Collection<S> sources,
                                                                     final Function<S, P> elementMapper,
                                                                     final Supplier<Collector<P, ?, C>> collectorSupplier) {
        final Collector<P, ?, C> collector = collectorSupplier.get();
        return sources.stream()
                .map(elementMapper)
                .collect(collector);
    }
}
