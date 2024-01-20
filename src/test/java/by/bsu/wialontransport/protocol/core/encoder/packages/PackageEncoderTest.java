package by.bsu.wialontransport.protocol.core.encoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Value;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class PackageEncoderTest {
    private final TestPackageEncoder encoder = new TestPackageEncoder();

    @Test
    public void encoderShouldBeAbleToEncodePackage() {
        final Package givenResponse = new TestPackage("value");

        final boolean actual = encoder.isAbleToEncode(givenResponse);
        final boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void encoderShouldNotBeAbleToEncodePackage() {
        final Package givenResponse = new Package() {
        };

        final boolean actual = encoder.isAbleToEncode(givenResponse);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void packageShouldBeEncoded() {
        final String givenValue = "value";
        final Package givenResponse = new TestPackage(givenValue);

        final String actual = encoder.encode(givenResponse);
        assertSame(givenValue, actual);
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeEncodedBecauseOfNotSuitableType() {
        final Package givenResponse = new Package() {
        };

        encoder.encode(givenResponse);
    }

    @Value
    private static class TestPackage implements Package {
        String value;
    }

    private static final class TestPackageEncoder extends PackageEncoder<TestPackage> {

        public TestPackageEncoder() {
            super(TestPackage.class);
        }

        @Override
        protected String encodeInternal(final TestPackage response) {
            return response.getValue();
        }
    }
}
