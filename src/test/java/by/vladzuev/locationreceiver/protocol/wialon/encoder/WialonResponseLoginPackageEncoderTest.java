package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonResponseLoginPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonResponseLoginPackageEncoderTest {
    private final WialonResponseLoginPackageEncoder encoder = new WialonResponseLoginPackageEncoder();

    @Test
    public void messageShouldBeGot() {
        final WialonResponseLoginPackage givenResponse = new WialonResponseLoginPackage(WialonResponseLoginPackage.Status.SUCCESS_AUTHORIZATION);

        final String actual = encoder.getMessage(givenResponse);
        final String expected = "1";
        assertEquals(expected, actual);
    }
}
