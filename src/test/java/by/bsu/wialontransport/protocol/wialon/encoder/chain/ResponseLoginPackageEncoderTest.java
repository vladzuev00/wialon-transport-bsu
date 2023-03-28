package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage.Status.SUCCESS_AUTHORIZATION;
import static org.junit.Assert.assertEquals;

public final class ResponseLoginPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private ResponseLoginPackageEncoder encoder;

    @Test
    public void packageShouldBeEncodedIndependentlyWithoutPostfix() {
        final ResponseLoginPackage givenPackage = new ResponseLoginPackage(SUCCESS_AUTHORIZATION);

        final String actual = this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
        final String expected = "#AL#1";
        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfixBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}
