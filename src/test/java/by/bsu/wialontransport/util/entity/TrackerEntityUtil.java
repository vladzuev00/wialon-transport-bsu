package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.TrackerEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class TrackerEntityUtil {

    public static void checkEquals(final TrackerEntity expected, final TrackerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getImei(), actual.getImei());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getUser(), actual.getUser());
    }
}
