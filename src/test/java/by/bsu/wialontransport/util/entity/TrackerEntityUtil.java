package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.TrackerEntity;
import lombok.experimental.UtilityClass;

import java.util.Collection;

import static by.bsu.wialontransport.util.CollectionUtil.areAllMatch;
import static by.bsu.wialontransport.util.HibernateUtil.isPropertyLoaded;
import static org.junit.Assert.assertEquals;

@UtilityClass
public final class TrackerEntityUtil {

    public static void checkEquals(final TrackerEntity expected, final TrackerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getImei(), actual.getImei());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getMileage(), actual.getMileage());
    }

    public static boolean isUserLoaded(final TrackerEntity entity) {
        return isPropertyLoaded(entity, TrackerEntity::getUser);
    }

    public static boolean isMileageLoaded(final TrackerEntity entity) {
        return isPropertyLoaded(entity, TrackerEntity::getMileage);
    }

    public static boolean areUsersNotLoaded(final Collection<TrackerEntity> entities) {
        return areAllMatch(entities, entity -> !isUserLoaded(entity));
    }

    public static boolean areMileagesNotLoaded(final Collection<TrackerEntity> entities) {
        return areAllMatch(entities, entity -> !isMileageLoaded(entity));
    }
}
