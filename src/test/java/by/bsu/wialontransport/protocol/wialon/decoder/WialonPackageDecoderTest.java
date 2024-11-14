package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonPackageDecoderTest {
    private final TestWialonPackageDecoder decoder = new TestWialonPackageDecoder();

    @Test
    public void contentShouldBeDecodedInternally() {
        final String givenContent = "message\r\n";

        final WialonPackage actual = decoder.decodeInternal(givenContent);
        final TestPackage expected = new TestPackage("message");
        assertEquals(expected, actual);
    }

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    private static final class TestPackage implements WialonPackage {
        private final String message;

        @Override
        public String getPrefix() {
            throw new UnsupportedOperationException();
        }
    }

    private static final class TestWialonPackageDecoder extends WialonPackageDecoder {
        private static final String REQUIRED_PREFIX = "#PREFIX#";

        public TestWialonPackageDecoder() {
            super(REQUIRED_PREFIX);
        }

        @Override
        protected TestPackage decodeMessage(final String message) {
            return new TestPackage(message);
        }
    }
}
