package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseBlackBoxPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class ResponseBlackBoxPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private ResponseBlackBoxPackageEncoder encoder;

    @Test
    public void packageShouldBeEncodedIndependentlyWithoutPostfix() {
        final ResponseBlackBoxPackage givenPackage = new ResponseBlackBoxPackage(10);

        final String actual = this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
        final String expected = "#AB#10";
        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfixBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}