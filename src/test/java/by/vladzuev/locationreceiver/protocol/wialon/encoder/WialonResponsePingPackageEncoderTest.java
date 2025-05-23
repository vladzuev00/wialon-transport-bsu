package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.ping.WialonResponsePingPackage;
import org.junit.jupiter.api.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.encoder.WialonResponsePingPackageEncoder.ENCODED_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class WialonResponsePingPackageEncoderTest {
    private final WialonResponsePingPackageEncoder encoder = new WialonResponsePingPackageEncoder();

    @Test
    public void messageShouldBeGot() {
        final WialonResponsePingPackage givenResponse = new WialonResponsePingPackage();

        final String actual = encoder.getMessage(givenResponse);
        assertSame(ENCODED_MESSAGE, actual);
    }
}
