package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonResponsePingPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class WialonResponsePingPackageEncoderTest {
    private final WialonResponsePingPackageEncoder encoder = new WialonResponsePingPackageEncoder();

    @Test
    public void messageShouldBeEncoded() {
        final WialonResponsePingPackage givenResponse = new WialonResponsePingPackage();

        final String actual = encoder.encodeMessage(givenResponse);
        final String expected = "";
        assertEquals(expected, actual);
    }
}
