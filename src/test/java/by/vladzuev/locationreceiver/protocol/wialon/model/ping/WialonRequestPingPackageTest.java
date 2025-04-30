package by.vladzuev.locationreceiver.protocol.wialon.model.ping;

import org.junit.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.ping.WialonRequestPingPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonRequestPingPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final WialonRequestPingPackage givenPackage = new WialonRequestPingPackage();

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
