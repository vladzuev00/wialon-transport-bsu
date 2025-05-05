package by.vladzuev.locationreceiver.protocol.teltonika.encoder.login;

import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseLoginPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class TeltonikaLoginPackageEncoderTest {
    private final TestTeltonikaLoginPackageEncoder encoder = new TestTeltonikaLoginPackageEncoder();

    @Test
    public void packageShouldBeEncodedInternally() {
        final TestPackage givenResponse = new TestPackage((byte) 10);

        final byte[] actual = encoder.encodeInternal(givenResponse);
        final byte[] expected = {10};
        assertArrayEquals(expected, actual);
    }

    private static final class TestPackage extends TeltonikaResponseLoginPackage {

        public TestPackage(final byte value) {
            super(value);
        }
    }

    private static final class TestTeltonikaLoginPackageEncoder extends TeltonikaLoginPackageEncoder<TestPackage> {

        public TestTeltonikaLoginPackageEncoder() {
            super(TestPackage.class);
        }
    }
}
