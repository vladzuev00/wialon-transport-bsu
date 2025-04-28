package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonResponseBulkLocationPackageEncoderTest {
    private final WialonResponseBulkLocationPackageEncoder encoder = new WialonResponseBulkLocationPackageEncoder();

    @Test
    public void messageShouldBeGot() {
        final WialonResponseBulkLocationPackage givenResponse = new WialonResponseBulkLocationPackage(5);

        final String actual = encoder.getMessage(givenResponse);
        final String expected = "5";
        assertEquals(expected, actual);
    }
}
