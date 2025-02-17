package by.vladzuev.locationreceiver.util;

import by.vladzuev.locationreceiver.crud.entity.AbstractEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;

import static org.hibernate.Hibernate.isInitialized;

@UtilityClass
public final class HibernateTestUtil {

    public static <E extends AbstractEntity<?>, P> boolean areEntityPropertiesLoaded(final List<E> entities,
                                                                                     final Function<E, P> propertyExtractor) {
        return entities.stream().allMatch(entity -> isPropertyLoaded(entity, propertyExtractor));
    }

    private static <E extends AbstractEntity<?>, P> boolean isPropertyLoaded(final E entity,
                                                                             final Function<E, P> propertyExtractor) {
        final P property = propertyExtractor.apply(entity);
        return isInitialized(property);
    }
}
