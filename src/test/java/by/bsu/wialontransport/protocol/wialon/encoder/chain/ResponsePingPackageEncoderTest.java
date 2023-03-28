package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.ResponsePingPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class ResponsePingPackageEncoderTest extends AbstractContextTest {

    @Autowired
    private ResponsePingPackageEncoder encoder;

    @Test
    public void packageShouldBeEncodedIndependentlyWithoutPostfix() {
        final ResponsePingPackage givenPackage = new ResponsePingPackage();

        final String actual = this.encoder.encodeIndependentlyWithoutPostfix(givenPackage);
        final String expected = "#AP#";
        assertEquals(expected, actual);
    }
}
