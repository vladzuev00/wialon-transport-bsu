package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.location.response.WialonResponseSingleLocationPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonResponseSingleLocationPackageEncoderTest {
    private final WialonResponseSingleLocationPackageEncoder encoder = new WialonResponseSingleLocationPackageEncoder();

    @Test
    public void messageShouldBeGot() {
        final var givenResponse = new WialonResponseSingleLocationPackage(WialonResponseSingleLocationPackage.Status.PACKAGE_FIX_SUCCESS);

        final String actual = encoder.getMessage(givenResponse);
        final String expected = "1";
        assertEquals(expected, actual);
    }
}
