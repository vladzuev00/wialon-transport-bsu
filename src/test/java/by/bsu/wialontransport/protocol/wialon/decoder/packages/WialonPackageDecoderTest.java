package by.bsu.wialontransport.protocol.wialon.decoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public final class WialonPackageDecoderTest {

    @Test
    public void sourceShouldBeDecoded() {
        final String givenPrefix = "#TEST#";
        final WialonPackage givenDecodedPackage = mock(WialonPackage.class);
        final TestWialonPackageDecoder givenDecoder = new TestWialonPackageDecoder(givenPrefix, givenDecodedPackage);

        final String givenSource = "#TEST#message\r\n";

        final WialonPackage actual = givenDecoder.decode(givenSource);
        assertSame(givenDecodedPackage, actual);

        final String actualCapturedMessage = givenDecoder.capturedMessage;
        final String expectedCapturedMessage = "message";
        assertEquals(expectedCapturedMessage, actualCapturedMessage);
    }

    private static final class TestWialonPackageDecoder extends WialonPackageDecoder {
        private final WialonPackage decodedPackage;

        private String capturedMessage;

        public TestWialonPackageDecoder(final String packagePrefix, final WialonPackage decodedPackage) {
            super(packagePrefix);
            this.decodedPackage = decodedPackage;
        }

        @Override
        protected WialonPackage decodeMessage(final String message) {
            capturedMessage = message;
            return decodedPackage;
        }
    }
}
