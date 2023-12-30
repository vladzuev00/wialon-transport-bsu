package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.TrackerEntity;
import lombok.experimental.UtilityClass;

import java.util.Collection;

import static by.bsu.wialontransport.util.CollectionUtil.areAllMatch;
import static by.bsu.wialontransport.util.HibernateUtil.isPropertyFetched;
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
        assertEquals(expected.getLastData(), actual.getLastData());
    }

    public static boolean isUserFetched(final TrackerEntity entity) {
        return isPropertyFetched(entity, TrackerEntity::getUser);
    }

    public static boolean isMileageFetched(final TrackerEntity entity) {
        return isPropertyFetched(entity, TrackerEntity::getMileage);
    }

    public static boolean isLastDataFetched(final TrackerEntity entity) {
        return isPropertyFetched(entity, TrackerEntity::getLastData);
    }

    public static boolean areUsersNotFetched(final Collection<TrackerEntity> entities) {
        return areAllMatch(entities, entity -> !isUserFetched(entity));
    }

    public static boolean areMileagesNotFetched(final Collection<TrackerEntity> entities) {
        return areAllMatch(entities, entity -> !isMileageFetched(entity));
    }

    public static boolean areLastDataNotFetched(final Collection<TrackerEntity> entities) {
        return areAllMatch(entities, entity -> !isLastDataFetched(entity));
    }
}
