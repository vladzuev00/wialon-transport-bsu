package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseDataPackage;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.Assert.assertEquals;

public final class WialonResponseDataPackageEncoderTest {
    private final WialonResponseDataPackageEncoder encoder = new WialonResponseDataPackageEncoder();

    @Test
    public void messageShouldBeEncoded() {
        final WialonResponseDataPackage givenResponse = new WialonResponseDataPackage(PACKAGE_FIX_SUCCESS);

        final String actual = encoder.encodeMessage(givenResponse);
        final String expected = "1";
        assertEquals(expected, actual);
    }
}
