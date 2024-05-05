package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.stream;

@UtilityClass
public final class CollectionUtil {

    public static <T> boolean areAllMatch(final Collection<T> elements, final Predicate<T> predicate) {
        return elements.stream().allMatch(predicate);
    }

    public static <T> boolean areAllNotMatch(final Collection<T> elements, final Predicate<T> predicate) {
        return elements.stream().noneMatch(predicate);
    }

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
        return mapAndCollect(sources, elementMapper, toList());
    }

    public static <S, P> Set<P> mapToSet(final Collection<S> sources, final Function<S, P> elementMapper) {
        return mapAndCollect(sources, elementMapper, toUnmodifiableSet());
    }

    public static <T> Optional<T> findLast(final List<T> elements) {
        final int indexLastElement = elements.size() - 1;
        return !elements.isEmpty() ? Optional.of(elements.get(indexLastElement)) : empty();
    }

    //TODO: refactor with StreamUtil
    public static <T> List<T> collectToList(final Iterator<T> iterator) {
        final Iterable<T> iterable = () -> iterator;
        final Spliterator<T> spliterator = iterable.spliterator();
        return stream(spliterator, false).toList();
    }

    public static <K, V> Map<K, V> convertToMap(final Collection<V> sources, final Function<V, K> keyExtractor) {
        return sources.stream()
                .collect(
                        toMap(
                                keyExtractor,
                                identity()
                        )
                );
    }

    public static <S, R> List<R> collectValuesToList(final Map<?, S> sourceMap, final Function<S, R> valueMapper) {
        final Collection<S> source = sourceMap.values();
        return mapToList(source, valueMapper);
    }

    public static <T> List<T> concat(final T first, final List<T> values) {
        return Stream.concat(
                Stream.of(first),
                values.stream()
        ).toList();
    }

    public static <T> List<T> concat(final List<T> first, final List<T> second) {
        return Stream.concat(
                first.stream(),
                second.stream()
        ).toList();
    }

//    public static <S, P> Optional<P> findGeneralProperty(final List<S> sources, final Function<S, P> propertyExtractor) {
//        final Set<P> properties = mapToSet(sources, propertyExtractor);
//        if(properties.size() != 1)
//    }

    private static <V> V throwExceptionOnKeyDuplication(final V existing, final V replacement) {
        throw new IllegalArgumentException("Key duplication was found when collection to map");
    }

    public static <S, P, D> D mapAndCollect(final Collection<S> sources,
                                            final Function<S, P> elementMapper,
                                            final Collector<P, ?, D> collector) {
        return sources.stream()
                .map(elementMapper)
                .collect(collector);
    }
}
