package by.vladzuev.locationreceiver.protocol.core.encoder.packages;

import lombok.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class StringPackageEncoderTest {
    private final TestStringPackageEncoder encoder = new TestStringPackageEncoder();

    @Test
    public void responseShouldBeDecodedInternally() {
        final TestPackage givenPackage = new TestPackage("test-response");

        final byte[] actual = encoder.encodeInternal(givenPackage);
        final byte[] expected = {116, 101, 115, 116, 45, 114, 101, 115, 112, 111, 110, 115, 101};
        assertArrayEquals(expected, actual);
    }

    @Value
    private static class TestPackage {
        String value;
    }

    private static final class TestStringPackageEncoder extends StringPackageEncoder<TestPackage> {

        public TestStringPackageEncoder() {
            super(TestPackage.class);
        }

        @Override
        protected String getString(final TestPackage response) {
            return response.getValue();
        }
    }
}
