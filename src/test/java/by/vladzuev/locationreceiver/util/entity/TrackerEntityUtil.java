package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
import by.vladzuev.locationreceiver.util.CollectionUtil;
import by.vladzuev.locationreceiver.util.HibernateUtil;
import lombok.experimental.UtilityClass;
import org.junit.Assert;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class TrackerEntityUtil {

    public static void assertEquals(final TrackerEntity expected, final TrackerEntity actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getImei(), actual.getImei());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        Assert.assertEquals(expected.getUser(), actual.getUser());
        Assert.assertEquals(expected.getMileage(), actual.getMileage());
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
