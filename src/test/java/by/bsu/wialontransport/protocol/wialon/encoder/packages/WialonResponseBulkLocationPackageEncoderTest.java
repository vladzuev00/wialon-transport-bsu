package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class WialonResponseBulkLocationPackageEncoderTest {
    private final WialonResponseBlackBoxPackageEncoder encoder = new WialonResponseBlackBoxPackageEncoder();

    @Test
    public void messageShouldBeEncoded() {
        final WialonResponseBulkLocationPackage givenResponse = new WialonResponseBulkLocationPackage(5);

        final String actual = encoder.encodeMessage(givenResponse);
        final String expected = "5";
        assertEquals(expected, actual);
    }
}
