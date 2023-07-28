package by.bsu.wialontransport.util.collection;

import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public static <S, P> List<P> extractProperties(final List<S> sources, final Function<S, P> propertyExtractor) {
        return sources.stream()
                .map(propertyExtractor)
                .toList();
    }

    private static <V> V throwExceptionOnKeyDuplication(final V existing, final V replacement) {
        throw new IllegalArgumentException("Key duplication was found when collection to map");
    }

}
