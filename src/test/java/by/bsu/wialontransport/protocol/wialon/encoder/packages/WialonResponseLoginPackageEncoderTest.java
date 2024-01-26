package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage.Status.SUCCESS_AUTHORIZATION;
import static org.junit.Assert.assertEquals;

public final class WialonResponseLoginPackageEncoderTest {
    private final WialonResponseLoginPackageEncoder encoder = new WialonResponseLoginPackageEncoder();

    @Test
    public void messageShouldBeEncoded() {
        final WialonResponseLoginPackage givenResponse = new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);

        final String actual = encoder.encodeMessage(givenResponse);
        final String expected = "1";
        assertEquals(expected, actual);
    }
}
