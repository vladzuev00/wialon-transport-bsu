package by.vladzuev.locationreceiver.protocol.wialon.model.login;

import org.junit.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonRequestLoginPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonRequestLoginPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final var givenPackage = new WialonRequestLoginPackage("11112222333344445555", "111");

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
