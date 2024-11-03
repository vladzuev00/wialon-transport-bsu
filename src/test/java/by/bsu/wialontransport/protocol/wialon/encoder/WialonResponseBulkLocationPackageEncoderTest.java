package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonResponseBulkLocationPackageEncoderTest {
    private final WialonResponseBulkLocationPackageEncoder encoder = new WialonResponseBulkLocationPackageEncoder();

    @Test
    public void messageShouldBeEncoded() {
        final WialonResponseBulkLocationPackage givenResponse = new WialonResponseBulkLocationPackage(5);

        final String actual = encoder.encodeMessage(givenResponse);
        final String expected = "5";
        assertEquals(expected, actual);
    }
}
