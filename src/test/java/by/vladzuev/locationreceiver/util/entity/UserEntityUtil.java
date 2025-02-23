package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import lombok.experimental.UtilityClass;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class UserEntityUtil {

    public static void assertEquals(final UserEntity expected, final UserEntity actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        assertSame(expected.getRole(), actual.getRole());
    }
}
