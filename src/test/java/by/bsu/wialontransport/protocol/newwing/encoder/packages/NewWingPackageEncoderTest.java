package by.bsu.wialontransport.protocol.newwing.encoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.response.ResponseNewWingPackage;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class NewWingPackageEncoderTest {
    private final TestResponseNewWingPackageEncoder encoder = new TestResponseNewWingPackageEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        final String givenValue = "value";
        final ResponseNewWingPackage givenResponse = new ResponseNewWingPackage(givenValue) {
        };

        final String actual = encoder.encodeInternal(givenResponse);
        assertSame(givenValue, actual);
    }

    private static final class TestResponseNewWingPackageEncoder extends NewWingPackageEncoder<ResponseNewWingPackage> {

        public TestResponseNewWingPackageEncoder() {
            super(ResponseNewWingPackage.class);
        }
    }
}
