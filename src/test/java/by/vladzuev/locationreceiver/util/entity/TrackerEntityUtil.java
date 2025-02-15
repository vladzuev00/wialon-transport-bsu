package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
import by.vladzuev.locationreceiver.util.CollectionUtil;
import by.vladzuev.locationreceiver.util.HibernateUtil;
import lombok.experimental.UtilityClass;

import java.util.Collection;

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

    public static boolean isUserFetched(final TrackerEntity entity) {
        return HibernateUtil.isPropertyFetched(entity, TrackerEntity::getUser);
    }

    public static boolean isMileageFetched(final TrackerEntity entity) {
        return HibernateUtil.isPropertyFetched(entity, TrackerEntity::getMileage);
    }

    public static boolean areUsersNotFetched(final Collection<TrackerEntity> entities) {
        return CollectionUtil.areAllMatch(entities, entity -> !isUserFetched(entity));
    }

    public static boolean areMileagesNotFetched(final Collection<TrackerEntity> entities) {
        return CollectionUtil.areAllMatch(entities, entity -> !isMileageFetched(entity));
    }
}
