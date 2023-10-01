package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Arrays.stream;

@UtilityClass
public final class EnumUtil {

    public static <V, E extends Enum<E>> Optional<E> findByValue(final V value,
                                                                 final Function<E, V> valueExtractor,
                                                                 final Class<E> enumType) {
        return stream(enumType.getEnumConstants())
                .filter(constant -> Objects.equals(valueExtractor.apply(constant), value))
                .findAny();
    }

    public static <V, E extends Enum<E>> E findByValueOrDefault(final V value,
                                                                final Function<E, V> valueExtractor,
                                                                final Class<E> enumType,
                                                                final E defaultConstant) {
        final Optional<E> optionalConstant = findByValue(value, valueExtractor, enumType);
        return optionalConstant.orElse(defaultConstant);
    }

}
