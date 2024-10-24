package by.bsu.wialontransport.protocol.newwing.encoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.response.NewWingResponsePackage;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class NewWingPackageEncoderTest {
    private final TestResponseNewWingPackageEncoder encoder = new TestResponseNewWingPackageEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        final String givenValue = "value";
        final NewWingResponsePackage givenResponse = new NewWingResponsePackage(givenValue) {
        };

        final String actual = encoder.encodeInternal(givenResponse);
        assertSame(givenValue, actual);
    }

    private static final class TestResponseNewWingPackageEncoder extends NewWingPackageEncoder<NewWingResponsePackage> {

        public TestResponseNewWingPackageEncoder() {
            super(NewWingResponsePackage.class);
        }
    }
}
