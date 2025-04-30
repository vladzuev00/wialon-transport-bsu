package by.vladzuev.locationreceiver.protocol.wialon.model.login;

import org.junit.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonResponseLoginPackage.PREFIX;
import static by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonResponseLoginPackage.Status.SUCCESS_AUTHORIZATION;
import static org.junit.Assert.assertSame;

public final class WialonResponseLoginPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final WialonResponseLoginPackage givenPackage = new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
