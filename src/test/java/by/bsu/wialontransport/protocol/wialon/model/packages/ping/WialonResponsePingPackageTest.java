package by.bsu.wialontransport.protocol.wialon.model.packages.ping;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonResponsePingPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonResponsePingPackageTest {

    @Test
    public void prefixShouldBeFound() {
        final WialonResponsePingPackage givenPackage = new WialonResponsePingPackage();

        final String actual = givenPackage.findPrefix();
        assertSame(PREFIX, actual);
    }
}