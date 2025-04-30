package by.vladzuev.locationreceiver.protocol.wialon.decoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.ping.WialonRequestPingPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class WialonRequestPingPackageDecoderTest {
    private final WialonRequestPingPackageDecoder decoder = new WialonRequestPingPackageDecoder();

    @Test
    public void messageShouldBeDecoded() {
        final String givenMessage = "";

        final WialonRequestPingPackage actual = decoder.decodeMessage(givenMessage);
        assertNotNull(actual);
    }
}
