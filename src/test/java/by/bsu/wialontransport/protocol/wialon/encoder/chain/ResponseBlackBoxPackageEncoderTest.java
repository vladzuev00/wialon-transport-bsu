package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class ResponseBlackBoxPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private ResponseBlackBoxPackageEncoder encoder;

    @Test
    public void packageShouldBeEncodedIndependentlyWithoutPostfix() {
        final WialonResponseBlackBoxPackage givenPackage = new WialonResponseBlackBoxPackage(10);

        final String actual = this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
        final String expected = "#AB#10";
        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfixBecauseOfNotSuitableType() {
        final WialonPackage givenPackage = new WialonPackage() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}
