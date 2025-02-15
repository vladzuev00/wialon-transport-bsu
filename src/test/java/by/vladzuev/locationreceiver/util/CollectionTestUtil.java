package by.vladzuev.locationreceiver.util;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public final class CollectionTestUtil {

    public static <K, V> Map<K, V> createLinkedHashMap(final K firstKey, final V firstValue,
                                                       final K secondKey, final V secondValue) {
        final Map<K, V> result = new LinkedHashMap<>();
        result.put(firstKey, firstValue);
        result.put(secondKey, secondValue);
        return result;
    }

    public static <K, V> Map<K, V> createLinkedHashMap(final K firstKey, final V firstValue,
                                                       final K secondKey, final V secondValue,
                                                       final K thirdKey, final V thirdValue) {
        final Map<K, V> result = createLinkedHashMap(firstKey, firstValue, secondKey, secondValue);
        result.put(thirdKey, thirdValue);
        return result;
    }

    public static <P, T> List<P> extractProperties(final List<T> sources, final Function<T, P> propertyExtractor) {
        return sources.stream()
                .map(propertyExtractor)
                .toList();
    }

}
