package by.vladzuev.locationreceiver.crud.enumeration;

import org.junit.jupiter.api.Test;

import static by.vladzuev.locationreceiver.crud.enumeration.UserRole.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class UserRoleTest {

    @Test
    public void authorityShouldBeGot() {
        final String actual = USER.getAuthority();
        final String expected = "USER";
        assertEquals(expected, actual);
    }
}
