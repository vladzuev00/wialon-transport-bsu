package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseBlackBoxPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class WialonResponseBlackBoxPackageEncoderTest {
    private final WialonResponseBlackBoxPackageEncoder encoder = new WialonResponseBlackBoxPackageEncoder();

    @Test
    public void messageShouldBeEncoded() {
        final WialonResponseBlackBoxPackage givenResponse = new WialonResponseBlackBoxPackage(5);

        final String actual = encoder.encodeMessage(givenResponse);
        final String expected = "5";
        assertEquals(expected, actual);
    }
}
