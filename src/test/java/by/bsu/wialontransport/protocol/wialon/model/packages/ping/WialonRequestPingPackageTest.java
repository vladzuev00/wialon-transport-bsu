package by.bsu.wialontransport.protocol.wialon.model.packages.ping;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonRequestPingPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final WialonRequestPingPackage givenPackage = new WialonRequestPingPackage();

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
