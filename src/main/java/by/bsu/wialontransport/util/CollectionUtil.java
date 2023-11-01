package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

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

    public static <T> Optional<T> findLast(final List<T> elements) {
        final int indexLastElement = elements.size() - 1;
        return !elements.isEmpty() ? Optional.of(elements.get(indexLastElement)) : empty();
    }

    //TODO: refactor with StreamUtil
    public static <T> List<T> convertToList(final Iterator<T> iterator) {
        final Iterable<T> iterable = () -> iterator;
        final Spliterator<T> spliterator = iterable.spliterator();
        return stream(spliterator, false).collect(toList());
    }

//    public static <S, P> Optional<P> findGeneralProperty(final List<S> sources, final Function<S, P> propertyExtractor) {
//        final Set<P> properties = mapToSet(sources, propertyExtractor);
//        if(properties.size() != 1)
//    }

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
