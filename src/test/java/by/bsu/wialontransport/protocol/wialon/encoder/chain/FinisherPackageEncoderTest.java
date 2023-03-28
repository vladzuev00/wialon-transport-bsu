package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public final class FinisherPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private FinisherPackageEncoder encoder;

    @Test(expected = UnsupportedOperationException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfix() {
        final Package givenPackage = new Package() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}
