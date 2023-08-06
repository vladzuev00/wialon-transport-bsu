package by.bsu.wialontransport.util.collection;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;

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

    public static <S, P> List<P> mapToList(final List<S> sources, final Function<S, P> elementMapper) {
        return sources.stream()
                .map(elementMapper)
                .toList();
    }

    public static <S, P> Set<P> mapToSet(final List<S> sources, final Function<S, P> elementMapper) {
        final List<P> elements = mapToList(sources, elementMapper);
        return new HashSet<>(elements);
    }

    private static <V> V throwExceptionOnKeyDuplication(final V existing, final V replacement) {
        throw new IllegalArgumentException("Key duplication was found when collection to map");
    }

}
