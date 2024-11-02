package by.bsu.wialontransport.protocol.core.encoder.packages;

import lombok.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class PackageEncoderTest {
    private final TestPackageEncoder encoder = new TestPackageEncoder();

    @Test
    public void encoderShouldBeAbleEncode() {
        final Object givenResponse = new TestPackage(null);

        assertTrue(encoder.isAbleEncode(givenResponse));
    }

    @Test
    public void encoderShouldNotBeAbleEncode() {
        final Object givenResponse = new Object();

        assertFalse(encoder.isAbleEncode(givenResponse));
    }

    @Test
    public void responseShouldBeEncoded() {
        final byte[] givenBytes = {1, 2, 3, 4, 5};
        final Object givenResponse = new TestPackage(givenBytes);

        final byte[] actual = encoder.encode(givenResponse);
        assertSame(givenBytes, actual);
    }

    @Test
    public void responseShouldNotBeEncoded() {
        final Object givenResponse = new Object();

        assertThrows(ClassCastException.class, () -> encoder.encode(givenResponse));
    }

    @Value
    private static class TestPackage {
        byte[] bytes;
    }

    private static final class TestPackageEncoder extends PackageEncoder<TestPackage> {

        public TestPackageEncoder() {
            super(TestPackage.class);
        }

        @Override
        protected byte[] encodeInternal(final TestPackage response) {
            return response.getBytes();
        }
    }
}
