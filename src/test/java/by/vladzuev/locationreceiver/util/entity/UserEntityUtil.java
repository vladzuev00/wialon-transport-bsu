package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class UserEntityUtil {

    public static void checkEquals(final UserEntity expected, final UserEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertSame(expected.getRole(), actual.getRole());
    }
}
