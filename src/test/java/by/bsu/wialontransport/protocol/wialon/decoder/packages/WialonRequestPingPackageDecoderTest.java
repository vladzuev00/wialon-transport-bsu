package by.bsu.wialontransport.protocol.wialon.decoder.packages;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class WialonRequestPingPackageDecoderTest {
    private final WialonRequestPingPackageDecoder decoder = new WialonRequestPingPackageDecoder();

    @Test
    public void messageShouldBeDecoded() {
        final String givenMessage = "";

        final WialonRequestPingPackage actual = this.decoder.decodeMessage(givenMessage);
        assertNotNull(actual);
    }
}
