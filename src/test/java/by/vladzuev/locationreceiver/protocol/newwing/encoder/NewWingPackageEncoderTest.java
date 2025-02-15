package by.vladzuev.locationreceiver.protocol.newwing.encoder;

import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingResponsePackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class NewWingPackageEncoderTest {
    private final TestNewWingPackageEncoder encoder = new TestNewWingPackageEncoder();

    @Test
    public void stringShouldBeGot() {
        final String givenValue = "test-response";
        final TestPackage givenResponse = new TestPackage(givenValue);

        final String actual = encoder.getString(givenResponse);
        assertSame(givenValue, actual);
    }

    private static final class TestPackage extends NewWingResponsePackage {

        public TestPackage(final String value) {
            super(value);
        }
    }

    private static final class TestNewWingPackageEncoder extends NewWingPackageEncoder<TestPackage> {

        public TestNewWingPackageEncoder() {
            super(TestPackage.class);
        }
    }
}
