package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class WialonPackageEncoderTest {
    private final TestWialonPackageEncoder encoder = new TestWialonPackageEncoder();

    @Test
    public void responseShouldBeEncodedInternally() {
        final TestWialonPackage givenResponse = new TestWialonPackage("message");

        final String actual = encoder.encodeInternal(givenResponse);
        final String expected = "#T#message\r\n";
        assertEquals(expected, actual);
    }

    @RequiredArgsConstructor
    @Getter
    private static final class TestWialonPackage extends WialonPackage {
        private static final String PREFIX = "#T#";

        private final String message;

        @Override
        public String getPrefix() {
            return PREFIX;
        }
    }

    private static final class TestWialonPackageEncoder extends WialonPackageEncoder<TestWialonPackage> {

        public TestWialonPackageEncoder() {
            super(TestWialonPackage.class);
        }

        @Override
        protected String encodeMessage(final TestWialonPackage response) {
            return response.message;
        }
    }
}
