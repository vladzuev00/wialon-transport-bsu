package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.Map;

@UtilityClass
public final class CollectionUtil {

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

}
