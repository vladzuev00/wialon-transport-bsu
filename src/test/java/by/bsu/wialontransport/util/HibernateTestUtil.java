package by.bsu.wialontransport.util;

import by.bsu.wialontransport.crud.entity.Entity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;

import static org.hibernate.Hibernate.isInitialized;

@UtilityClass
public final class HibernateTestUtil {

    public static <E extends Entity<?>, P> boolean areEntityPropertiesLoaded(final List<E> entities,
                                                                             final Function<E, P> propertyExtractor) {
        return entities.stream().allMatch(entity -> isPropertyLoaded(entity, propertyExtractor));
    }

    private static <E extends Entity<?>, P> boolean isPropertyLoaded(final E entity,
                                                                     final Function<E, P> propertyExtractor) {
        final P property = propertyExtractor.apply(entity);
        return isInitialized(property);
    }
}
