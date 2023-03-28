package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.Assert.assertEquals;

public final class ResponseDataPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private ResponseDataPackageEncoder encoder;

    @Test
    public void packageShouldBeEncodedIndependentlyWithoutPostfix() {
        final Package givenPackage = new ResponseDataPackage(PACKAGE_FIX_SUCCESS);

        final String actual = this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
        final String expected = "#AD#1";
        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeEncodedIndependentlyWithoutPostfix() {
        final Package givenPackage = new Package() {
        };

        this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
    }
}
