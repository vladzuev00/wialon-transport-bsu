package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedPackageDecoderTest {
    private final TestPrefixedPackageDecoder decoder = new TestPrefixedPackageDecoder();

    @Test
    public void decoderShouldBeAbleToDecodeBuffer() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("5052454649586753"));

        assertTrue(decoder.isAbleDecode(givenBuffer));
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeBuffer() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("5052444649586753"));

        assertFalse(decoder.isAbleDecode(givenBuffer));
    }

    @Test
    public void sourceShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("505245464958746573742d6d657373616765"));

        final Object actual = decoder.decode(givenBuffer);
        final Object expected = new TestPackage("test-message");
        assertEquals(expected, actual);
    }

    @Value
    private static class TestPackage {
        String message;
    }

    private static final class TestPrefixedPackageDecoder extends PrefixedPackageDecoder<ByteBuf, String> {
        private static final String REQUIRED_PREFIX = "PREFIX";
        private static final int MESSAGE_BYTE_COUNT = 12;

        public TestPrefixedPackageDecoder() {
            super(REQUIRED_PREFIX);
        }

        @Override
        protected int getLength(final String prefix) {
            return prefix.length();
        }

        @Override
        protected String readPrefix(final ByteBuf buffer, final int length) {
            return buffer.readCharSequence(length, US_ASCII).toString();
        }

        @Override
        protected ByteBuf skip(final ByteBuf buffer, final int length) {
            return buffer.skipBytes(length);
        }

        @Override
        protected Object decodeInternal(final ByteBuf buffer) {
            final String message = readMessage(buffer);
            return new TestPackage(message);
        }

        private String readMessage(final ByteBuf buffer) {
            return buffer.readCharSequence(MESSAGE_BYTE_COUNT, US_ASCII).toString();
        }
    }
}
