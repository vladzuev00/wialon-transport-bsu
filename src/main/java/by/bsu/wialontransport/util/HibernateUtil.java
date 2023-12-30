package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

import static org.hibernate.Hibernate.isInitialized;

@UtilityClass
public final class HibernateUtil {

    public static boolean isFetched(final Object object) {
        return object != null && isInitialized(object);
    }

    public static <T> boolean isPropertyFetched(final T object, final Function<T, Object> getter) {
        final Object property = getter.apply(object);
        return isFetched(property);
    }

}
