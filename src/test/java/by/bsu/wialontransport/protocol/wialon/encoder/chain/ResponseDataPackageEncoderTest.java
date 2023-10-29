package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.Assert.assertEquals;

public final class ResponseDataPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private ResponseDataPackageEncoder encoder;

    @Test
    public void packageShouldBeEncodedIndependentlyWithoutPostfix() {
        final WialonPackage givenPackage = new WialonResponseDataPackage(PACKAGE_FIX_SUCCESS);

        final String actual = this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
        final String expected = "#AD#1";
        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfix() {
        final WialonPackage givenPackage = new WialonPackage() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}
