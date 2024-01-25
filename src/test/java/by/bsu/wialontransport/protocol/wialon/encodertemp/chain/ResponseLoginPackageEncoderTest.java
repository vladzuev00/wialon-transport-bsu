package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage.Status.SUCCESS_AUTHORIZATION;
import static org.junit.Assert.assertEquals;

public final class ResponseLoginPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private ResponseLoginPackageEncoder encoder;

    @Test
    public void packageShouldBeEncodedIndependentlyWithoutPostfix() {
        final WialonResponseLoginPackage givenPackage = new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);

        final String actual = this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
        final String expected = "#AL#1";
        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfixBecauseOfNotSuitableType() {
        final WialonPackage givenPackage = new WialonPackage() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}
