package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public final class StarterPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private StarterPackageEncoder encoder;

    @Test(expected = UnsupportedOperationException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfix() {
        final WialonPackage givenPackage = new WialonPackage() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}
