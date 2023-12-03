package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import static org.hibernate.Hibernate.isInitialized;

@UtilityClass
public final class HibernateUtil {

    public static boolean isLoaded(final Object object) {
        return object != null && isInitialized(object);
    }

}
