package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import lombok.Getter;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public final class WialonPackageDecoderTest {

    @Test
    public void decoderShouldBeAbleToDecodeSource() {
        final String givenPackagePrefix = "#TEST#";
        final WialonPackageDecoder<?> givenDecoder = new TestWialonPackageDecoder(
                givenPackagePrefix,
                null
        );

        final String givenSource = "#TEST#message\r\n";

        final boolean actual = givenDecoder.isAbleToDecode(givenSource);
        assertTrue(actual);
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeSource() {
        final String givenPackagePrefix = "#TEST#";
        final WialonPackageDecoder<?> givenDecoder = new TestWialonPackageDecoder(
                givenPackagePrefix,
                null
        );

        final String givenSource = "#TESTS#message\r\n";

        final boolean actual = givenDecoder.isAbleToDecode(givenSource);
        assertFalse(actual);
    }

    @Test
    public void sourceShouldBeDecoded() {
        final String givenPackagePrefix = "#TEST#";
        final WialonPackage givenDecodedPackage = mock(WialonPackage.class);
        final TestWialonPackageDecoder givenDecoder = new TestWialonPackageDecoder(
                givenPackagePrefix,
                givenDecodedPackage
        );

        final String givenSource = "#TEST#message\r\n";

        final WialonPackage actual = givenDecoder.decode(givenSource);
        assertSame(givenDecodedPackage, actual);

        final String expectedCapturedMessage = "message";
        assertEquals(expectedCapturedMessage, givenDecoder.getCapturedMessage());
    }

    private static final class TestWialonPackageDecoder extends WialonPackageDecoder<WialonPackage> {
        private final WialonPackage decodedPackage;

        @Getter
        private String capturedMessage;

        public TestWialonPackageDecoder(final String packagePrefix, final WialonPackage decodedPackage) {
            super(packagePrefix);
            this.decodedPackage = decodedPackage;
        }

        @Override
        protected WialonPackage decodeMessage(final String message) {
            this.capturedMessage = message;
            return this.decodedPackage;
        }
    }
}
